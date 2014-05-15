/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projettransmondyn;

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
