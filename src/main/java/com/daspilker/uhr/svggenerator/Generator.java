/*
   Copyright 2012 Daniel A. Spilker

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.daspilker.uhr.svggenerator;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import static java.awt.geom.Arc2D.OPEN;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import static java.lang.String.format;
import static java.util.Arrays.copyOfRange;
import static java.util.Locale.ENGLISH;
import static org.apache.batik.dom.svg.SVGDOMImplementation.SVG_NAMESPACE_URI;
import static org.apache.batik.dom.svg.SVGDOMImplementation.getDOMImplementation;
import static org.apache.batik.util.SVGConstants.SVG_HEIGHT_ATTRIBUTE;
import static org.apache.batik.util.SVGConstants.SVG_SVG_TAG;
import static org.apache.batik.util.SVGConstants.SVG_VIEW_BOX_ATTRIBUTE;
import static org.apache.batik.util.SVGConstants.SVG_WIDTH_ATTRIBUTE;

public class Generator {
    private static final Stroke STROKE_CUT = new BasicStroke(0.01f);
    private static final Color COLOR_CUT = BLUE;
    private static final Color COLOR_BACKGROUND = BLACK;
    private static final Color COLOR_GLYPHS = WHITE;

    private String fontName;
    private float fontSize;
    private String[] text;
    private double width;
    private double height;
    private double borderX;
    private double borderY;
    private double lightShadeExtra;
    private double frameWidth;
    private double screwDiameter;
    private double connectorDiameter;
    private double wireDiameter;
    private double nutSize;
    private double depth;
    private double connectorDistance;
    private double anodesConnectorOffsetX;
    private double anodesConnectorOffsetY;
    private double cathodesConnectorOffsetX;
    private double cathodesConnectorOffsetY;
    private double pcbScrew1X;
    private double pcbScrew1Y;
    private double pcbScrew2X;
    private double pcbScrew2Y;
    private double pcbScrew3X;
    private double pcbScrew3Y;
    private double ledPinWidth;
    private double ledPinHeight;
    private double ledPinDistanceX;
    private double ledPinDistanceY;
    private String outputFileNameFront;
    private String outputFileNameLightShades;
    private String outputFileNameLightShadeRaster;
    private String outputFileNameDistanceRaster1;
    private String outputFileNameDistanceRaster2;
    private String outputFileNameLedCarrier;
    private boolean fill;

    private int rows;
    private int columns;
    private double maxCharacterHeight;
    private double firstColumnWidth;
    private double lastColumnWidth;
    private double factorX;
    private double factorY;
    private double innerWidth;
    private double innerHeight;
    private double size;

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setBorderX(double borderX) {
        this.borderX = borderX;
    }

    public void setBorderY(double borderY) {
        this.borderY = borderY;
    }

    public void setScrewDiameter(double screwDiameter) {
        this.screwDiameter = screwDiameter;
    }

    public void setConnectorDiameter(double connectorDiameter) {
        this.connectorDiameter = connectorDiameter;
    }

    public void setWireDiameter(double wireDiameter) {
        this.wireDiameter = wireDiameter;
    }

    public void setNutSize(double nutSize) {
        this.nutSize = nutSize;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setConnectorDistance(double connectorDistance) {
        this.connectorDistance = connectorDistance;
    }

    public void setAnodesConnectorOffsetX(double anodesConnectorOffsetX) {
        this.anodesConnectorOffsetX = anodesConnectorOffsetX;
    }

    public void setAnodesConnectorOffsetY(double anodesConnectorOffsetY) {
        this.anodesConnectorOffsetY = anodesConnectorOffsetY;
    }

    public void setCathodesConnectorOffsetX(double cathodesConnectorOffsetX) {
        this.cathodesConnectorOffsetX = cathodesConnectorOffsetX;
    }

    public void setCathodesConnectorOffsetY(double cathodesConnectorOffsetY) {
        this.cathodesConnectorOffsetY = cathodesConnectorOffsetY;
    }

    public void setPcbScrew1X(double pcbScrew1X) {
        this.pcbScrew1X = pcbScrew1X;
    }

    public void setPcbScrew1Y(double pcbScrew1Y) {
        this.pcbScrew1Y = pcbScrew1Y;
    }

    public void setPcbScrew2X(double pcbScrew2X) {
        this.pcbScrew2X = pcbScrew2X;
    }

    public void setPcbScrew2Y(double pcbScrew2Y) {
        this.pcbScrew2Y = pcbScrew2Y;
    }

    public void setPcbScrew3X(double pcbScrew3X) {
        this.pcbScrew3X = pcbScrew3X;
    }

    public void setPcbScrew3Y(double pcbScrew3Y) {
        this.pcbScrew3Y = pcbScrew3Y;
    }

    public void setLedPinWidth(double ledPinWidth) {
        this.ledPinWidth = ledPinWidth;
    }

    public void setLedPinHeight(double ledPinHeight) {
        this.ledPinHeight = ledPinHeight;
    }

    public void setLedPinDistanceX(double ledPinDistanceX) {
        this.ledPinDistanceX = ledPinDistanceX;
    }

    public void setLedPinDistanceY(double ledPinDistanceY) {
        this.ledPinDistanceY = ledPinDistanceY;
    }

    public void setOutputFileNameFront(String outputFileNameFront) {
        this.outputFileNameFront = outputFileNameFront;
    }

    public void setOutputFileNameLightShades(String outputFileNameLightShades) {
        this.outputFileNameLightShades = outputFileNameLightShades;
    }

    public void setOutputFileNameLightShadeRaster(String outputFileNameLightShadeRaster) {
        this.outputFileNameLightShadeRaster = outputFileNameLightShadeRaster;
    }

    public void setOutputFileNameDistanceRaster1(String outputFileNameDistanceRaster1) {
        this.outputFileNameDistanceRaster1 = outputFileNameDistanceRaster1;
    }

    public void setOutputFileNameDistanceRaster2(String outputFileNameDistanceRaster2) {
        this.outputFileNameDistanceRaster2 = outputFileNameDistanceRaster2;
    }

    public void setOutputFileNameLedCarrier(String outputFileNameLedCarrier) {
        this.outputFileNameLedCarrier = outputFileNameLedCarrier;
    }

    public void setLightShadeExtra(double lightShadeExtra) {
        this.lightShadeExtra = lightShadeExtra;
    }

    public void setFrameWidth(double frameWidth) {
        this.frameWidth = frameWidth;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public void generate() throws IOException {
        Font font = Font.decode(fontName).deriveFont(fontSize);

        SVGGraphics2D svgGraphics2D = createSVGGraphics2D();
        Map<Character, Path2D> glyphOutlines = getGlyphOutlines(font, svgGraphics2D.getFontRenderContext());
        computeDerivedValues(glyphOutlines);
        drawFront(svgGraphics2D, glyphOutlines);
        writeToFile(svgGraphics2D, outputFileNameFront);

        svgGraphics2D = createSVGGraphics2D();
        drawLightShade(svgGraphics2D);
        writeToFile(svgGraphics2D, outputFileNameLightShades);

        svgGraphics2D = createSVGGraphics2D();
        drawLightShadeRaster(svgGraphics2D);
        writeToFile(svgGraphics2D, outputFileNameLightShadeRaster);

        svgGraphics2D = createSVGGraphics2D();
        drawDistanceRaster1(svgGraphics2D);
        writeToFile(svgGraphics2D, outputFileNameDistanceRaster1);

        svgGraphics2D = createSVGGraphics2D();
        drawDistanceRaster2(svgGraphics2D);
        writeToFile(svgGraphics2D, outputFileNameDistanceRaster2);

        svgGraphics2D = createSVGGraphics2D();
        drawLedCarrier(svgGraphics2D);
        writeToFile(svgGraphics2D, outputFileNameLedCarrier);
    }

    private void computeDerivedValues(Map<Character, Path2D> glyphOutlines) {
        maxCharacterHeight = computeMaxGlyphHeight(glyphOutlines);
        firstColumnWidth = computeFirstColumnWidth(glyphOutlines);
        lastColumnWidth = computeLastColumnWidth(glyphOutlines);
        factorX = (width - 2.0 * borderX - firstColumnWidth / 2.0 - lastColumnWidth / 2.0) / (text[0].length() - 1.0);
        factorY = maxCharacterHeight + (height - 2.0 * borderY - text.length * maxCharacterHeight) / (text.length - 1.0);
        innerWidth = width - 2.0 * frameWidth;
        innerHeight = height - 2.0 * frameWidth;
        size = max(maxCharacterHeight, computeMaxGlyphWidth(glyphOutlines)) + lightShadeExtra;
        rows = text.length;
        columns = text[0].length();
    }

    private SVGGraphics2D createSVGGraphics2D() {
        Document document = getDOMImplementation().createDocument(SVG_NAMESPACE_URI, SVG_SVG_TAG, null);
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
        ctx.setComment(null);
        SVGGraphics2D svgGraphics2D = new SVGGraphics2D(ctx, true);
        svgGraphics2D.setStroke(STROKE_CUT);
        return svgGraphics2D;
    }

    private Map<Character, Path2D> getGlyphOutlines(Font font, FontRenderContext fontRenderContext) {
        Set<Character> letters = new HashSet<>();
        for (String line : text) {
            for (int j = 0; j < line.length(); j += 1) {
                letters.add(line.charAt(j));
            }
        }

        Map<Character, Path2D> result = new HashMap<>();
        for (char c : letters) {
            GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, new char[]{c});
            Path2D glyphOutline = (Path2D) glyphVector.getGlyphOutline(0);
            glyphOutline.transform(getTranslateInstance(-glyphOutline.getBounds2D().getX(), 0.0));
            result.put(c, glyphOutline);
        }
        return result;
    }

    private void drawFront(Graphics2D graphics2D, Map<Character, Path2D> glyphOutlines) {
        double offsetX = borderX + firstColumnWidth / 2.0;
        double offsetY = borderY + maxCharacterHeight;

        if (fill) {
            graphics2D.setColor(COLOR_BACKGROUND);
            graphics2D.fill(new Rectangle2D.Double(0.0, 0.0, width, height));
        } else {
            graphics2D.setColor(COLOR_CUT);
            graphics2D.draw(new Rectangle2D.Double(0.0, 0.0, width, height));
        }

        for (int i = 0; i < rows; i += 1) {
            double ty = i * factorY + offsetY;
            for (int j = 0; j < columns; j += 1) {
                Path2D glyphOutline = glyphOutlines.get(text[i].charAt(j));
                Rectangle2D bounds = glyphOutline.getBounds2D();
                double tx = j * factorX + offsetX - bounds.getWidth() / 2.0;
                Path2D transformedShape = (Path2D) glyphOutline.createTransformedShape(getTranslateInstance(tx, ty));
                if (fill) {
                    graphics2D.setColor(COLOR_GLYPHS);
                    graphics2D.fill(transformedShape);
                } else {
                    graphics2D.setColor(COLOR_CUT);
                    graphics2D.draw(transformedShape);
                }
            }
        }
    }

    private void drawLightShade(Graphics2D graphics2D) {
        graphics2D.setColor(COLOR_CUT);
        double incircleRadius = size / 2.0;
        for (int i = 0; i < columns; i += 1) {
            double centerX = i * size * sin(toRadians(60));
            for (int j = 0; j < rows; j += 1) {
                if (i % 2 == 0) {
                    if (j == 0) {
                        graphics2D.draw(createHexagon(centerX, j * size, incircleRadius));
                    } else {
                        graphics2D.draw(createHexagonFraction(centerX, j * size, incircleRadius, new int[]{210, 270, 330, 30, 90, 150}));
                    }
                } else {
                    graphics2D.draw(createHexagonFraction(centerX, (j + 0.5) * size, incircleRadius, new int[]{150, 210}));
                    if (j == text.length - 1) {
                        graphics2D.draw(createHexagonFraction(centerX, (j + 0.5) * size, incircleRadius, new int[]{270, 330, 30, 90}));
                    }
                }
            }
        }
    }

    private void drawLightShadeRaster(Graphics2D graphics2D) {
        graphics2D.setColor(COLOR_CUT);
        graphics2D.draw(new Rectangle2D.Double(0.0, 0.0, innerWidth, innerHeight));
        drawLedHoles(graphics2D, size);
        graphics2D.draw(new Line2D.Double(0.0, innerHeight, 0.0, innerHeight + depth * 2.0));
        graphics2D.draw(new Line2D.Double(0.0, innerHeight + depth * 2.0, innerWidth, innerHeight + depth * 2.0));
        graphics2D.draw(new Line2D.Double(innerWidth, innerHeight + depth * 2.0, innerWidth, innerHeight));
        graphics2D.draw(new Line2D.Double(0.0, innerHeight + depth, innerWidth, innerHeight + depth));
        graphics2D.draw(new Line2D.Double(innerWidth, 0.0, innerWidth + 2.0 * depth, 0.0));
        graphics2D.draw(new Line2D.Double(innerWidth + 2.0 * depth, 0.0, innerWidth + 2.0 * depth, innerHeight + 2.0 * frameWidth));
        graphics2D.draw(new Line2D.Double(innerWidth + 2.0 * depth, innerHeight + 2.0 * frameWidth, innerWidth, innerHeight + 2.0 * frameWidth));
        graphics2D.draw(new Line2D.Double(innerWidth + depth, 0.0, innerWidth + depth, innerHeight + 2.0 * frameWidth));
    }

    private void drawDistanceRaster1(Graphics2D graphics2D) {
        double extra = connectorDistance * 0.5;
        double offsetX = innerWidth - borderX + frameWidth + (connectorDistance + ledPinDistanceX - lastColumnWidth) / 2.0;
        double offsetY = borderY - frameWidth - (connectorDistance - ledPinDistanceY - maxCharacterHeight) / 2.0;
        double nutExtra = nutSize / sqrt(3.0) + extra;

        graphics2D.setColor(COLOR_CUT);
        drawLedHoles(graphics2D, size - lightShadeExtra);
        drawScrewHoles(graphics2D);
        Path2D.Double path = new Path2D.Double();
        path.moveTo(0.0, 0.0);
        path.lineTo(0.0, offsetY);
        path.lineTo(anodesConnectorOffsetX + connectorDistance * 0.5, offsetY);
        for (int i = 1; i < rows - 2; i += 1) {
            path.lineTo(anodesConnectorOffsetX + connectorDistance * (i - 0.5), offsetY + factorY * i);
            path.lineTo(anodesConnectorOffsetX + connectorDistance * (i + 0.5), offsetY + factorY * i);
        }
        path.lineTo(anodesConnectorOffsetX + connectorDistance * (rows - 2.5), innerHeight - anodesConnectorOffsetY - extra);
        path.lineTo(anodesConnectorOffsetX + connectorDistance * (rows - 1) + extra, innerHeight - anodesConnectorOffsetY - extra);
        path.lineTo(anodesConnectorOffsetX + connectorDistance * (rows - 1) + extra, innerHeight - pcbScrew2Y - nutExtra);
        path.append(new Arc2D.Double(pcbScrew2X - nutExtra, innerHeight - pcbScrew2Y - nutExtra, 2.0 * nutExtra, 2.0 * nutExtra, 90.0, -180.0, OPEN), true);
        path.lineTo(anodesConnectorOffsetX + connectorDistance * (rows - 1) + extra, innerHeight - pcbScrew2Y + nutExtra);
        path.lineTo(anodesConnectorOffsetX + connectorDistance * (rows - 1) + extra, innerHeight - cathodesConnectorOffsetY - connectorDistance * (columns - 0.5));
        for (int i = columns - 1; i > 0; i -= 1) {
            path.lineTo(offsetX - factorX * i, innerHeight - cathodesConnectorOffsetY - connectorDistance * (i + 0.5));
            path.lineTo(offsetX - factorX * i, innerHeight - cathodesConnectorOffsetY - connectorDistance * (i - 0.5));
        }
        path.lineTo(offsetX, innerHeight - cathodesConnectorOffsetY - connectorDistance * 0.5);
        path.lineTo(offsetX, innerHeight);
        path.lineTo(innerWidth, innerHeight);
        path.lineTo(innerWidth, 0.0);
        path.closePath();
        graphics2D.draw(path);
    }

    private void drawDistanceRaster2(Graphics2D graphics2D) {
        double offsetX = borderX + firstColumnWidth / 2.0 - frameWidth;
        double offsetY = borderY + maxCharacterHeight / 2.0 - frameWidth;
        double screwDistanceX1 = offsetX + 0.5 * factorX;
        double screwDistanceY1 = offsetY + 0.5 * factorY;
        double screwDistanceX2 = offsetX + 3.5 * factorX;
        double screwDistanceY2 = offsetY + 2.5 * factorY;

        graphics2D.setColor(COLOR_CUT);
        graphics2D.draw(new Rectangle2D.Double(0.0, 0.0, innerWidth, innerHeight));
        drawLedHoles(graphics2D, size);
        drawPcbHoles(graphics2D);
        graphics2D.draw(createHexagon(screwDistanceX1, screwDistanceY1, nutSize / 2.0));
        graphics2D.draw(createHexagon(innerWidth - screwDistanceX1, screwDistanceY1, nutSize / 2.0));
        graphics2D.draw(createHexagon(screwDistanceX1, innerHeight - screwDistanceY1, nutSize / 2.0));
        graphics2D.draw(createHexagon(innerWidth - screwDistanceX1, innerHeight - screwDistanceY1, nutSize / 2.0));
        graphics2D.draw(createHexagon(screwDistanceX2, screwDistanceY2, nutSize / 2.0));
        graphics2D.draw(createHexagon(innerWidth - screwDistanceX2, screwDistanceY2, nutSize / 2.0));
        graphics2D.draw(createHexagon(screwDistanceX2, innerHeight - screwDistanceY2, nutSize / 2.0));
        graphics2D.draw(createHexagon(innerWidth - screwDistanceX2, innerHeight - screwDistanceY2, nutSize / 2.0));
    }

    private void drawLedCarrier(Graphics2D graphics2D) {
        double offsetXLeft = borderX + firstColumnWidth / 2.0 - frameWidth;
        double offsetXRight = borderX + lastColumnWidth / 2.0 - frameWidth;
        double offsetY = borderY + maxCharacterHeight / 2.0 - frameWidth;

        graphics2D.setColor(COLOR_CUT);
        graphics2D.draw(new Rectangle2D.Double(0.0, 0.0, innerWidth, innerHeight));
        for (int i = 0; i < rows; i += 1) {
            double ty = i * factorY + offsetY;
            for (int j = 0; j < columns; j += 1) {
                double tx = j * factorX + offsetXLeft;
                graphics2D.draw(createLedFootPrint(tx, ty));
            }
        }
        drawScrewHoles(graphics2D);

        for (int i = 0; i < rows; i += 1) {
            graphics2D.draw(new Ellipse2D.Double(anodesConnectorOffsetX + connectorDistance * i - connectorDiameter / 2.0, innerHeight - anodesConnectorOffsetY - connectorDiameter / 2.0, connectorDiameter, connectorDiameter));
            graphics2D.draw(new Ellipse2D.Double(anodesConnectorOffsetX + connectorDistance * i - wireDiameter / 2.0, offsetY + factorY * i - (wireDiameter - ledPinDistanceY) / 2.0, wireDiameter, wireDiameter));
        }
        for (int i = 0; i < columns; i += 1) {
            graphics2D.draw(new Ellipse2D.Double(cathodesConnectorOffsetX - connectorDiameter / 2.0, innerHeight - cathodesConnectorOffsetY - connectorDistance * i - connectorDiameter / 2.0, connectorDiameter, connectorDiameter));
            graphics2D.draw(new Ellipse2D.Double(innerWidth - offsetXRight - factorX * i - (wireDiameter - ledPinDistanceX) / 2.0, innerHeight - cathodesConnectorOffsetY - connectorDistance * i - wireDiameter / 2.0, wireDiameter, wireDiameter));
        }
        drawPcbHoles(graphics2D);
    }

    private void drawPcbHoles(Graphics2D graphics2D) {
        graphics2D.draw(new Ellipse2D.Double(pcbScrew1X - screwDiameter / 2.0, innerHeight - pcbScrew1Y - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(pcbScrew2X - screwDiameter / 2.0, innerHeight - pcbScrew2Y - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(pcbScrew3X - screwDiameter / 2.0, innerHeight - pcbScrew3Y - screwDiameter / 2.0, screwDiameter, screwDiameter));
    }

    private void drawLedHoles(Graphics2D graphics2D, double incircleDiameter) {
        double offsetX = borderX + firstColumnWidth / 2.0 - frameWidth;
        double offsetY = borderY + maxCharacterHeight / 2.0 - frameWidth;

        for (int i = 0; i < rows; i += 1) {
            double ty = i * factorY + offsetY;
            for (int j = 0; j < columns; j += 1) {
                double tx = j * factorX + offsetX;
                graphics2D.draw(createHexagon(tx, ty, incircleDiameter / 2.0));
            }
        }
    }

    private void drawScrewHoles(Graphics2D graphics2D) {
        double offsetX = borderX + firstColumnWidth / 2.0 - frameWidth;
        double offsetY = borderY + maxCharacterHeight / 2.0 - frameWidth;
        double screwDistanceX1 = offsetX + 0.5 * factorX;
        double screwDistanceY1 = offsetY + 0.5 * factorY;
        double screwDistanceX2 = offsetX + 3.5 * factorX;
        double screwDistanceY2 = offsetY + 2.5 * factorY;

        graphics2D.draw(new Ellipse2D.Double(screwDistanceX1 - screwDiameter / 2.0, screwDistanceY1 - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(innerWidth - screwDistanceX1 - screwDiameter / 2.0, screwDistanceY1 - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(screwDistanceX1 - screwDiameter / 2.0, innerHeight - screwDistanceY1 - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(innerWidth - screwDistanceX1 - screwDiameter / 2.0, innerHeight - screwDistanceY1 - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(screwDistanceX2 - screwDiameter / 2.0, screwDistanceY2 - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(innerWidth - screwDistanceX2 - screwDiameter / 2.0, screwDistanceY2 - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(screwDistanceX2 - screwDiameter / 2.0, innerHeight - screwDistanceY2 - screwDiameter / 2.0, screwDiameter, screwDiameter));
        graphics2D.draw(new Ellipse2D.Double(innerWidth - screwDistanceX2 - screwDiameter / 2.0, innerHeight - screwDistanceY2 - screwDiameter / 2.0, screwDiameter, screwDiameter));
    }

    private double computeFirstColumnWidth(Map<Character, Path2D> glyphOutlines) {
        double result = 0.0;
        for (String line : text) {
            Path2D glyphOutline = glyphOutlines.get(line.charAt(0));
            result = max(result, glyphOutline.getBounds2D().getWidth());
        }
        return result;
    }

    private double computeLastColumnWidth(Map<Character, Path2D> glyphOutlines) {
        double result = 0.0;
        for (String line : text) {
            Path2D glyphOutline = glyphOutlines.get(line.charAt(line.length() - 1));
            result = max(result, glyphOutline.getBounds2D().getWidth());
        }
        return result;
    }

    private void writeToFile(SVGGraphics2D svgGraphics2D, String outputFileName) throws IOException {
        Element svgRoot = svgGraphics2D.getRoot();
        svgRoot.setAttributeNS(null, SVG_WIDTH_ATTRIBUTE, format(ENGLISH, "%fmm", width));
        svgRoot.setAttributeNS(null, SVG_HEIGHT_ATTRIBUTE, format(ENGLISH, "%fmm", height));
        svgRoot.setAttributeNS(null, SVG_VIEW_BOX_ATTRIBUTE, format(ENGLISH, "%f %f %f %f", 0.0, 0.0, width, height));

        try (FileWriter writer = new FileWriter(outputFileName)) {
            svgGraphics2D.stream(svgRoot, writer);
        }
    }

    private Shape createLedFootPrint(double centerX, double centerY) {
        Path2D.Double path = new Path2D.Double();
        path.append(new Rectangle2D.Double((ledPinDistanceX - ledPinWidth) / 2.0, (ledPinDistanceY - ledPinHeight) / 2.0, ledPinWidth, ledPinHeight), false);
        path.append(new Rectangle2D.Double((-ledPinDistanceX - ledPinWidth) / 2.0, (ledPinDistanceY - ledPinHeight) / 2.0, ledPinWidth, ledPinHeight), false);
        path.append(new Rectangle2D.Double((ledPinDistanceX - ledPinWidth) / 2.0, (-ledPinDistanceY - ledPinHeight) / 2.0, ledPinWidth, ledPinHeight), false);
        path.append(new Rectangle2D.Double((-ledPinDistanceX - ledPinWidth) / 2.0, (-ledPinDistanceY - ledPinHeight) / 2.0, ledPinWidth, ledPinHeight), false);
        path.transform(getTranslateInstance(centerX, centerY));
        return path;
    }

    private static double computeMaxGlyphHeight(Map<Character, Path2D> glyphOutlines) {
        double result = 0.0;
        for (Path2D glyphOutline : glyphOutlines.values()) {
            result = max(result, glyphOutline.getBounds2D().getHeight());
        }
        return result;
    }

    private static double computeMaxGlyphWidth(Map<Character, Path2D> glyphOutlines) {
        double result = 0.0;
        for (Path2D glyphOutline : glyphOutlines.values()) {
            result = max(result, glyphOutline.getBounds2D().getWidth());
        }
        return result;
    }

    private static Shape createHexagon(double centerX, double centerY, double incircleRadius) {
        return createHexagonFraction(centerX, centerY, incircleRadius, new int[]{30, 90, 150, 210, 270, 330}, true);
    }

    private static Shape createHexagonFraction(double centerX, double centerY, double incircleRadius, int[] angles) {
        return createHexagonFraction(centerX, centerY, incircleRadius, angles, false);
    }

    private static Shape createHexagonFraction(double centerX, double centerY, double incircleRadius, int[] angles,
                                               boolean close) {
        double radius = 2.0 * incircleRadius / sqrt(3.0);
        Path2D.Double path = new Path2D.Double();
        path.moveTo(radius * sin(toRadians(angles[0])), radius * cos(toRadians(angles[0])));
        for (int angle : copyOfRange(angles, 1, angles.length)) {
            path.lineTo(radius * sin(toRadians(angle)), radius * cos(toRadians(angle)));
        }
        if (close) {
            path.closePath();
        }
        path.transform(getTranslateInstance(centerX, centerY));
        return path;
    }
}
