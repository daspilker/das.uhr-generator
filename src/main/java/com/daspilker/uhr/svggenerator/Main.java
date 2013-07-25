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

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] text = {
                "ESLISTEF\u00DCNF",
                "VIERTELZEHN",
                "ZWANZIGXVOR",
                "NACHDASHALB",
                "ZWEINSDREIS",
                "VIEROTNACHT",
                "ELFZW\u00D6LF\u00DCNF",
                "USECHSIEBEN",
                "ZEHNEUNDUHR"
        };

        Generator generator = new Generator();
        generator.setFontName("DIN Schablonierschrift");
        generator.setFontSize(16.0f);
        generator.setWidth(300.0);
        generator.setHeight(300.0);
        generator.setBorderX(35.0);
        generator.setBorderY(35.0);
        generator.setText(text);
        generator.setOutputFileNameFront("front.svg");
        generator.setOutputFileNameLightShades("shades.svg");
        generator.setOutputFileNameLightShadeRaster("raster.svg");
        generator.setOutputFileNameDistanceRaster1("distance1.svg");
        generator.setOutputFileNameDistanceRaster2("distance2.svg");
        generator.setOutputFileNameLedCarrier("led.svg");
        generator.setLightShadeExtra(4.0);
        generator.setFrameWidth(3.0);
        generator.setScrewDiameter(3.2);
        generator.setConnectorDiameter(0.7);
        generator.setWireDiameter(1.1);
        generator.setNutSize(5.5);
        generator.setDepth(44);
        generator.setConnectorDistance(2.54);
        generator.setAnodesConnectorOffsetX(1.905);
        generator.setAnodesConnectorOffsetY(77.47);
        generator.setCathodesConnectorOffsetX(31.115);
        generator.setCathodesConnectorOffsetY(1.905);
        generator.setPcbScrew1X(3.81);
        generator.setPcbScrew1Y(91.44);
        generator.setPcbScrew2X(26.035);
        generator.setPcbScrew2Y(50.165);
        generator.setPcbScrew3X(15.24);
        generator.setPcbScrew3Y(3.81);
        generator.setLedPinWidth(0.86);
        generator.setLedPinHeight(0.5);
        generator.setLedPinDistanceX(5.08);
        generator.setLedPinDistanceY(5.08);
        generator.setFill(false);
        generator.generate();
    }
}
