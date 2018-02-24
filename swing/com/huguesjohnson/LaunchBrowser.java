/*
LaunchBrowser.java - Launches a url in the OS default web browser
Copyright  (C) 2000-2007 Hugues Johnson
Portions of code (C) 2005 By Dem Pilafian (see http://www.centerkey.com/java/browser/)
 
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson;

import java.io.IOException;
import java.lang.reflect.Method;

/** 
 * LaunchBrowser attempts to launch the operating system default browser.<p>
 * Usage examples:<p>
 * <code>String result=(new LaunchBrowser()).launch("http://www.huguesjohnson.com");</code><p>
 * <code>String verboseResult=(new LaunchBrowser()).launch("http://www.huguesjohnson.com",true);</code><p>
 * <code>String verboseResult=(new LaunchBrowser()).launch("file://c:\\some-folder\\some-file.html",true);</code><p>
 * <code>String verboseResult=(new LaunchBrowser()).launch("file:///some-dir/some-file.html",true);</code><p>
 * <p>
 * This has only been tested on Windows 2000, if it doesn't work on another OS please set verbose to true to debug.
 * I can just about guarantee this will not work with OS/2, the algorithm to lookup the operating system explicitly checks for Windows and MacOS with the default being UNIX/Linux.
 * UNIX/Linux is considered the default because each "flavor" of UNIX/Linux returns a different name to the System.getProperty("os.name") call.. there is no standard name. For example, HP-UX returns "HP-UX" while Solaris returns "Solaris".
 * <p><p>
 * <b>Updated 2007-07-28</b>: Updated UNIX/Linux code with logic from BareBonesBrowserLaunch.java By Dem Pilafian (see http://www.centerkey.com/java/browser/).
 * The Windows and MacOS logic was basically the same between the original version of this class and BareBonesBrowserLaunch.java but his UNIX/Linux logic was more reliable.
 * @author Hugues Johnson
 */
public abstract class LaunchBrowser extends Object{
    private final static int OS_WINDOWS=0;
    private final static int OS_MAC=1;
    private final static int OS_UNIX=2;
    
    private final static String WINDOWS_START="win";
    private final static String MAC_START="mac";
    
    private final static String WINDOWS_COMMAND="rundll32";
    private final static String WINDOWS_PARAMETERS="url.dll,FileProtocolHandler";
    private final static String UNIX_COMMAND="netscape";
    private final static String UNIX_PARAMETERS="-remote openURL";
    
    /** 
     * Launches a web browser with the specified url.
     * First tries to use current browser session, if no browser is loaded it starts a new session.
     * See comments for the class for proper usage and known issues.
     * @param url The address to open.
     */
    public static String launch(String url){
        return(launch(url,false));
    }
    
    /** 
     * Launches a web browser with the specified url.
     * First tries to use current browser session, if no browser is loaded it starts a new session.
     * See comments for the class for proper usage and known issues.
     * @param url The address to open.
     * @param verbose Whether or not to return verbose output, useful for debugging. This is false if launch(url) is used.
     */
    public static String launch(String url,boolean verbose){
        StringBuffer result=new StringBuffer();
        if(verbose){ result.append("received url parameter: ").append(url); }
        int osValue=LaunchBrowser.lookupOperatingSystem();
        if(verbose){ result.append("\nlookupOperatingSystem() returned osValue: ").append(osValue); }
        switch(osValue){
            case OS_WINDOWS:{
                if(verbose){ result.append("\nOperating system is Windows"); }
                StringBuffer commandLine=new StringBuffer(WINDOWS_COMMAND).append(" ").append(WINDOWS_PARAMETERS).append(" ").append(url);
                if(verbose){ result.append("\nTrying to execute command line: ").append(commandLine); }
                try{
                    Process process=Runtime.getRuntime().exec(commandLine.toString());
                } catch(IOException iox){
                    if(verbose){ result.append("\n"); }
                    result.append("IOException occurred executing Windows command: ").append(iox.toString());
                }
                break;
            }
            case OS_UNIX:{
                if(verbose){ result.append("\nOperating system is UNIX or Linux"); }
                /* try to open url in current browser session first */
                StringBuffer commandLine=new StringBuffer(UNIX_COMMAND).append(" ").append(UNIX_PARAMETERS).append(" (").append(url).append(")");
                try{
                	String[] browsers={"firefox","opera","konqueror","epiphany","mozilla","netscape"}; 
                	String browser=null;
                	for(int count=0;count<browsers.length&&browser==null;count++){
                		if(Runtime.getRuntime().exec(new String[]{"which",browsers[count]}).waitFor()==0){
                			browser=browsers[count];
                		}
                	}
               		if(browser==null){ 
                        if(verbose){ result.append("\n"); }
               			result.append("Could not find web browser"); 
               		} else {
                        if(verbose){ result.append("\nUsing web browser: "); result.append(browser); }
               			Runtime.getRuntime().exec(new String[]{browser,url});
               		}
                } catch(IOException iox){
                    if(verbose){ result.append("\n"); }
                    result.append("IOException occurred executing UNIX command: ").append(iox.toString());
                } catch(InterruptedException ix){
                    if(verbose){ result.append("\n"); }
                    result.append("UNIX process interrupted: ").append(ix.toString());
                }
                break;
            }
            case OS_MAC:{
                if(verbose){ result.append("\nOperating system is MacOS"); }
                try{
                    Class mrjFileUtilsClass=Class.forName("com.apple.mrj.MRJFileUtils");
                    Object mrjFileUtilsInstance=mrjFileUtilsClass.newInstance();
                    Method method=mrjFileUtilsClass.getDeclaredMethod("openURL",new Class[]{String.class});
                    method.invoke(null, new Object[]{url});
                } catch(ClassNotFoundException cnfx){
                    if(verbose){ result.append("\n"); }
                    result.append("Unable to find com.apple.mrj.MRJFileUtils class: ").append(cnfx.toString());
                } catch(IllegalAccessException iax){
                    if(verbose){ result.append("\n"); }
                    result.append("Unable to access com.apple.mrj.MRJFileUtils class: ").append(iax.toString());
                } catch(InstantiationException ix){
                    if(verbose){ result.append("\n"); }
                    result.append("Unable to instantiate com.apple.mrj.MRJFileUtils class: ").append(ix.toString());
                } catch(Exception x){
                    if(verbose){ result.append("\n"); }
                    result.append("Exception thrown from MRJFileUtils: ").append(x.toString());
                }
                break;
            }
        }
        return(result.toString());
    }
    
    /** 
     * Looks up the operating system and returns one of the OS_ constants declared in this class.
     * Explicitly looks for Windows and MacOS, if the OS is neither than UNIX is considered the default.
     * See comments for class declaration for more details on the OS lookup.
     * @return A constant (defined in this class) representing the operating system.
     */
    private final static int lookupOperatingSystem(){
        String osName=System.getProperty("os.name").toLowerCase();
        int osValue=OS_UNIX;
        if(osName.startsWith(WINDOWS_START)){
            osValue=OS_WINDOWS;
        } else if(osName.startsWith(MAC_START)){
            osValue=OS_MAC;
        }
        return(osValue);
    }
}
