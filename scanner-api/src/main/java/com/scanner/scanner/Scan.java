package com.scanner.scanner;

import com.scanner.models.Answer;
import com.scanner.models.Coordinate;

import com.scanner.models.Question;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class Scan {

    private static ArrayList<Coordinate> coords1 = new ArrayList();
    private static ArrayList<Coordinate> coords2 = new ArrayList();
    private static ArrayList<Coordinate> coords3 = new ArrayList();

    public Scan() {
    }

    public static String readOCR(BufferedImage template, Coordinate coordinate) {
        Tesseract tess4j = new Tesseract();
        tess4j.setVariable("user_defined_dpi", "72");
        tess4j.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        tess4j.setLanguage("por");

        String result = null;
        try {
            result = tess4j.doOCR(template.getSubimage(coordinate.getX(),
                    coordinate.getY(),
                    coordinate.getWidth(),
                    coordinate.getHeight()));
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        assert result != null;
        result = result.replace("\n", "");
        coords1.add(coordinate);
        return result;
    }


    public static List<Answer> scanAll(BufferedImage binary, List<Question> coordinates, Integer templateId, String path) throws IOException {
        List<Answer> answers = new ArrayList<>();
        for (Question c : coordinates) {

            Answer answer = new Answer();
            answer.setTemplateId(templateId);
            answer.setQuestion(c);

            answer.setAnswer(scanA(binary, c.getCoordinate()));

                    System.out.println("Questao: " + c.getQuestionNumber() + " Resposta: " + scanA(binary, c.getCoordinate()));


            answers.add(answer);
        }

        drawRects(binary, String.valueOf(templateId), path);
        coords1 = new ArrayList<>();
        coords2 = new ArrayList<>();
        coords3 = new ArrayList<>();
        return answers;
    }

    public static int getX(BufferedImage binary, Coordinate coordinate) {
        Coordinate c = new Coordinate(coordinate.getY(),
                coordinate.getX(),
                coordinate.getWidth() / 2,
                coordinate.getHeight());

        int n = 0;
        for (int i = c.getX(); i <= c.getX() + c.getWidth(); i++) {
            for (int j = c.getY(); j <= c.getY() + c.getWidth(); j++) {
                int pixel = binary.getRGB(i, j);
                Color corDoPixel = new Color(pixel);
                if (corDoPixel.getRed() == 0 && corDoPixel.getGreen() == 0 && corDoPixel.getBlue() == 0) {
                    return i;
                }
            }
        }
        return n;
    }

    public static int getYY(BufferedImage binary, Coordinate coordinate) {
        Coordinate c = new Coordinate(coordinate.getY(),
                coordinate.getX(),
                coordinate.getWidth() / 2,
                coordinate.getHeight());

        int n = 0;
        for (int i = c.getY(); i <= c.getY() + c.getHeight(); i++) {
            for (int j = c.getX(); j <= c.getX() + c.getWidth(); j++) {
                int pixel = binary.getRGB(j, i);
                Color corDoPixel = new Color(pixel);
                if (corDoPixel.getRed() == 0 && corDoPixel.getGreen() == 0 && corDoPixel.getBlue() == 0) {
                    return i;
                }
            }
        }
        return n;
    }


    public static int getW(BufferedImage binary, Coordinate coordinate, int x) {
        Coordinate c = new Coordinate(coordinate.getY(),
                coordinate.getX(),
                coordinate.getWidth(),
                coordinate.getHeight());

        int n = 0;
        for (int i = c.getX(); i <= c.getX() + c.getWidth(); i++) {
            for (int j = c.getY(); j <= c.getY() + c.getHeight(); j++) {
                int pixel = binary.getRGB(i, j);
                Color corDoPixel = new Color(pixel);
                if (corDoPixel.getRed() == 0 && corDoPixel.getGreen() == 0 && corDoPixel.getBlue() == 0) {
                    n = i + 1;
                }
            }
        }
        return (n - x);
    }


    public static int getH(BufferedImage binary, Coordinate coordinate, int y) {
        Coordinate coordinateC = new Coordinate(coordinate.getY(),
                coordinate.getX(),
                coordinate.getWidth(),
                coordinate.getHeight());

        int n = 0;
        for (int j = coordinateC.getY(); j <= coordinateC.getY() + coordinateC.getHeight(); j++) {
            for (int i = coordinateC.getX(); i <= coordinateC.getX() + coordinateC.getWidth(); i++) {
                int pixel = binary.getRGB(i, j);
                Color corDoPixel = new Color(pixel);
                if (corDoPixel.getRed() == 0 && corDoPixel.getGreen() == 0 && corDoPixel.getBlue() == 0) {
                    n = j + 1;

                }
            }
        }
        return (n - y);
    }

    public static String scanA(BufferedImage binary, Coordinate coordinate) {
        coords1.add(coordinate);
        // coords.add(coordinateE);

        Coordinate coordinateC = new Coordinate(coordinate.getY(),
                coordinate.getX(),
                coordinate.getWidth() / 2,
                coordinate.getHeight());

        Coordinate coordinateE = new Coordinate(coordinate.getY(),
                coordinate.getX() + (coordinate.getWidth() / 2),
                coordinate.getWidth() / 2,
                coordinate.getHeight());


        int c;
        int e;

//TIPO A
        c = scanResult(binary, coordinateC);
        e = scanResult(binary, coordinateE);
//       System.out.println("Alternativa C: " + c);
//       System.out.println("Alternativa E: " + e);


        String resposta = "";
        if (c == 0 && e == 0) {
            resposta = "*";
        } else if (c == 0 && e == 1) {
            resposta = "E";
        } else if (c == 1 && e == 0) {
            resposta = "C";
        } else if (c == 1 && e == 1) {
            resposta = "?";
        } else {
            System.out.println("Erro!");
        }
        return resposta;


    }

    public static Integer scanResult(BufferedImage binary, Coordinate c) {
        double black = 0;
        double white = 0;
        double total = 0;
        double sobra = 0;
        int result;

        Coordinate cd = new Coordinate();
        cd.setX(Scan.getX(binary, c));
        cd.setY(Scan.getYY(binary, c));
        cd.setWidth(Scan.getW(binary, c, cd.getX()));
        cd.setHeight(Scan.getH(binary, c, cd.getY()));
        coords2.add(cd);

        for (int i = c.getX(); i <= c.getX() + c.getWidth(); i++) {
            for (int j = c.getY(); j <= c.getY() + c.getHeight(); j++) {
                total++;
                int pixel = binary.getRGB(i, j);
                Color corDoPixel = new Color(pixel);
                // System.out.println("Red:" + corDoPixel);
                //System.out.println(corDoPixel.toString() + "| x = "+ i + " | y = "+ j);
                if (corDoPixel.getRed() == 0 && corDoPixel.getGreen() == 0 && corDoPixel.getBlue() == 0) {

                    black++;
                } else if (corDoPixel.getRed() == 255 && corDoPixel.getGreen() == 255 && corDoPixel.getBlue() == 255) {
                    white++;
                }
            }
        }

//       System.out.println("Black: " + (black/total)*100 + "% " +" | White: " + (white/total)*100+ "% " );
//       System.out.println("--------------------------------------");
        if ((black / total) * 100 >= 30) {
            result = 1;
            coords3.add(cd);
        } else {
            result = 0;
        }
        return result;
    }


    public static void drawRects(BufferedImage binary,String fileName, String path) throws IOException {
        Graphics2D graphics2D = binary.createGraphics();

        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setPaint(Color.BLACK);


        for (Coordinate coordinate : coords1) {

            graphics2D.drawRect(coordinate.getX(), coordinate.getY(), coordinate.getWidth(), coordinate.getHeight());

        }


        graphics2D.setStroke(new BasicStroke(2));

        for (Coordinate q : coords2) {
            graphics2D.drawRect(q.getX(), q.getY(), q.getWidth(), q.getHeight());
        }


        graphics2D.setStroke(new BasicStroke(15));

        graphics2D.setPaint(Color.BLACK);
        for (Coordinate q : coords3) {
            graphics2D.drawRect(q.getX(), q.getY(), q.getWidth(), q.getHeight());

        }
        graphics2D.dispose();
        saveMappedImg(binary, fileName, path);

    }

    public static void saveMappedImg(BufferedImage binary, String fileName, String path) throws IOException {
        ImageIO.write(binary, "png", new File(path+"template/" + fileName + ".png"));
    }
}

