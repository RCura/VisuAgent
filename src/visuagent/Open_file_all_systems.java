/* 
 * Copyright (C) 2014 Mehdi Boukhechba
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package visuagent;

import java.io.File;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;

/**
 *
 * @author mehdi
 */
public class Open_file_all_systems 
{
  private static final String errMsg = "Error attempting to launch this file ";

   public static void openURL(String url) {
      String osName = System.getProperty("os.name");
      try {
         if (osName.startsWith("Mac OS")) {
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",
               new Class[] {String.class});
            openURL.invoke(null, new Object[] {new File(url).getAbsolutePath()});
            }
         else if (osName.startsWith("Windows")){
           Runtime.getRuntime().exec(new String[]
            {"rundll32", "url.dll,FileProtocolHandler",
             new File(url).getAbsolutePath()});
         
         }else { //assume Unix or Linux
           
               //Runtime.getRuntime().exec(new String[] {browser, url});
               Runtime.getRuntime().exec(new String[]{"xdg-open",new File(url).getAbsolutePath()});
            }
         }
      catch (Exception e) {
         JOptionPane.showMessageDialog(null, errMsg + ":\n" + e.getLocalizedMessage());
         }
      }

    
}
