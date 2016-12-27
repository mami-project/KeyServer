/*
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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Version class controller tests.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.1
 */
public class VersionTest {
    /**
     * Version test object 1.
     */
    private Version obj1;
    /**
     * Version test object 2.
     */
    private Version obj2;
    /**
     * Version test object 3.
     */
    private Version obj3;
    /**
     * Version test object 4.
     */
    private Version obj4;
    /**
     * Version test object 5.
     */
    private Version obj5;
    /**
     * Version test object 6.
     */
    private Version obj6;
    /**
     * Version test object 7.
     */
    private Version obj7;
    
    /**
     * Version Test class constructor.
     */
    public VersionTest() {
        obj1 = new Version("v1.0.0");
        obj2 = new Version("1.0.0");
        obj3 = new Version("1.1.0");
        obj4 = new Version("1.0.1");
        obj5 = new Version("0.1.0");
        obj6 = new Version("0.0.1");
        obj7 = new Version("v1.1.1");
    }

    /**
     * Test of getMajor method, of class Version.
     */
    @Test
    public void testGetMajor() {
        System.out.println("getMajor");
        Version instance = obj1;
        int expResult = 1;
        int result = instance.getMajor();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMinor method, of class Version.
     */
    @Test
    public void testGetMinor() {
        System.out.println("getMinor");
        Version instance = obj5;
        int expResult = 1;
        int result = instance.getMinor();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPatch method, of class Version.
     */
    @Test
    public void testGetPatch() {
        System.out.println("getPatch");
        Version instance = obj6;
        int expResult = 1;
        int result = instance.getPatch();
        assertEquals(expResult, result);
    }

    /**
     * Test of equalsTo method, of class Version.
     */
    @Test
    public void testEqualsTo1() {
        System.out.println("equalsTo1");
        Version extVer = obj1;
        Version instance = obj2;
        boolean expResult = true;
        boolean result = instance.equalsTo(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equalsTo method, of class Version.
     */
    @Test
    public void testEqualsTo2() {
        System.out.println("equalsTo2");
        Version extVer = obj1;
        Version instance = obj4;
        boolean expResult = false;
        boolean result = instance.equalsTo(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equalsTo method, of class Version.
     */
    @Test
    public void testEqualsTo3() {
        System.out.println("equalsTo3");
        Version extVer = obj1;
        Version instance = obj3;
        boolean expResult = false;
        boolean result = instance.equalsTo(extVer);
        assertEquals(expResult, result);
    }

    /**
     * Test of greaterThan method, of class Version.
     */
    @Test
    public void testGreaterThan1() {
        System.out.println("greaterThan1");
        Version extVer = obj5;
        Version instance = obj1;
        boolean expResult = true;
        boolean result = instance.greaterThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of greaterThan method, of class Version.
     */
    @Test
    public void testGreaterThan2() {
        System.out.println("greaterThan2");
        Version extVer = obj1;
        Version instance = obj5;
        boolean expResult = false;
        boolean result = instance.greaterThan(extVer);
        assertEquals(expResult, result);
    }

    /**
     * Test of greaterThan method, of class Version.
     */
    @Test
    public void testGreaterThan3() {
        System.out.println("greaterThan3");
        Version extVer = obj2;
        Version instance = obj3;
        boolean expResult = true;
        boolean result = instance.greaterThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of greaterThan method, of class Version.
     */
    @Test
    public void testGreaterThan4() {
        System.out.println("greaterThan4");
        Version extVer = obj3;
        Version instance = obj4;
        boolean expResult = false;
        boolean result = instance.greaterThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of greaterThan method, of class Version.
     */
    @Test
    public void testGreaterThan5() {
        System.out.println("greaterThan5");
        Version extVer = obj3;
        Version instance = obj7;
        boolean expResult = true;
        boolean result = instance.greaterThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of greaterThan method, of class Version.
     */
    @Test
    public void testGreaterThan6() {
        System.out.println("greaterThan6");
        Version extVer = obj7;
        Version instance = obj3;
        boolean expResult = false;
        boolean result = instance.greaterThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of lowerThan method, of class Version.
     */
    @Test
    public void testLowerThan1() {
        System.out.println("lowerThan1");
        Version extVer = obj5;
        Version instance = obj1;
        boolean expResult = false;
        boolean result = instance.lowerThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of lowerThan method, of class Version.
     */
    @Test
    public void testLowerThan2() {
        System.out.println("lowerThan2");
        Version extVer = obj1;
        Version instance = obj5;
        boolean expResult = true;
        boolean result = instance.lowerThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of lowerThan method, of class Version.
     */
    @Test
    public void testLowerThan3() {
        System.out.println("lowerThan3");
        Version extVer = obj4;
        Version instance = obj3;
        boolean expResult = false;
        boolean result = instance.lowerThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of lowerThan method, of class Version.
     */
    @Test
    public void testLowerThan4() {
        System.out.println("lowerThan4");
        Version extVer = obj3;
        Version instance = obj4;
        boolean expResult = true;
        boolean result = instance.lowerThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of lowerThan method, of class Version.
     */
    @Test
    public void testLowerThan5() {
        System.out.println("lowerThan5");
        Version extVer = obj3;
        Version instance = obj7;
        boolean expResult = false;
        boolean result = instance.lowerThan(extVer);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of lowerThan method, of class Version.
     */
    @Test
    public void testLowerThan6() {
        System.out.println("lowerThan6");
        Version extVer = obj7;
        Version instance = obj3;
        boolean expResult = true;
        boolean result = instance.lowerThan(extVer);
        assertEquals(expResult, result);
    }
}
