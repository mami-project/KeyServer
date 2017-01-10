/**
 * Copyright 2016.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.tid.keyserver.core.lib;

/**
 * Version class controller.
 *     This class provide an easy way for manipulate version strings or compare
 *     between them.
 * @see <a href="http://semver.org/">Semantic Version 2.0.0</a>
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.1
 */
public class Version {
    /**
     * Major version field vXX.xx.xx
     */
    private int major;

    /**
     * Minor version field vxx.XX.xx
     */
    private int minor;

    /**
     * Patch version field vxx.xx.XX
     */
    private int patch;
    
    /**
     * Class constructor.
     * @param strVer String with the version numbers. Example: v0.1.0
     * @since v0.4.1
     */
    public Version(String strVer){
        String tmp=strVer;
        if(tmp.startsWith("v")){
            tmp = tmp.substring(1);
        }
        /* The period / dot is a special character in regex, you have to escape 
            it either with a double backlash \\.     */
        String [] values = tmp.split("\\.");
        major = Integer.valueOf(values[0]);
        minor = Integer.valueOf(values[1]);
        patch = Integer.valueOf(values[2]);
    }
    
    /**
     * This method returns the major version field.
     * @return  Major version field as integer.
     * @since v0.4.1
     */
    public int getMajor(){
        return this.major;
    }
    
    /**
     * This method returns the minor version field.
     * @return Minor version field as integer.
     * @since v0.4.1
     */
    public int getMinor(){
        return this.minor;
    }
    
    /**
     * This method returns the patch version field.
     * @return Patch version field.
     * @since v0.4.1
     */
    public int getPatch(){
        return this.patch;
    }
    
    /**
     * This method is used to compare if two Version objects are equals
     *     (contains the same version fields).
     * @param extVer External Version object to be compared with the current 
     *     version object.
     * @return True if are equals.
     * @since v0.4.1
     */
    public boolean equalsTo(Version extVer){
        return ((extVer.getMajor() == this.major) && 
                (extVer.getMinor() == this.minor) && 
                (extVer.getPatch() == this.patch));
    }
    
    /**
     * Method used to compare two Version objects.
     *     This method compare if the current object is major than the external
     *     object.
     * @param extVer External version object used to be compared with the 
     *     current object.
     * @return True if the current object is greater than
     * @since v0.4.1
     */
    public boolean greaterThan(Version extVer){
        if(this.major > extVer.getMajor()){
            return true;
        } else if(this.major == extVer.getMajor()){
            if (this.minor > extVer.getMinor()) {
                return true;
            } else
                return this.minor == extVer.getMinor() && this.patch > extVer.getPatch();
        } else {
            return false;
        }
    }
    
    /**
     * Method used to compare two Version objects.
     *     This method compare if the current object is lower than the external
     *     object.
     * @param extVer External version object used to be compared with the 
     *     current object.
     * @return True if the current object is lower than
     * @since v0.4.1
     */
    public boolean lowerThan(Version extVer){
        if(this.major < extVer.getMajor()){
            return true;
        } else if(this.major == extVer.getMajor()){
            if (this.minor < extVer.getMinor()) {
                return true;
            } else
                return this.minor == extVer.getMinor() && this.patch < extVer.getPatch();
        } else {
            return false;
        }
    }
}
