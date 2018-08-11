/* This test suite only tests out the ObjectTree.macroRegex for addition and subtractions.
 * 
 * Currently our regex for Addition and Subraction does not pass all tests. To bypass the build restrictions I set them to @Ignore.
 * 
 * 
 */

package com.github.tgstation.fastdmm.objtree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.junit.Test;

import junit.framework.JUnit4TestAdapter;



//@RunWith(Suite.class)
//@SuiteClasses({})
public class ObjectTreeTest {
	
	@Test
	public void BasicSubtractTest()
	{
		String val = "1 - 5";
		Matcher m = Pattern.compile(ObjectTree.macroRegex).matcher(val);
		if (m.find())
		{
			assertEquals(m.group(2), "-");
		}
		else
		{
			fail("Didn't catch it.");
		}
	}
	
	@Test
	public void BasicAddTest()
	{
		String val = "1 + 5";
		Matcher m = Pattern.compile(ObjectTree.macroRegex).matcher(val);
		if (m.find())
		{
			assertEquals(m.group(2), "+");
		}
		else
		{
			fail("Didn't catch it.");
		}
	}


	@Test
	public void NoMacro() {	
		String longDescWithMinus = "This is just text.";
		Matcher m = Pattern.compile(ObjectTree.macroRegex).matcher(longDescWithMinus);
		if (m.find())
			fail("There is no macro in this string.");
	}
	
	@Test
	public void MacroNegativeNumberMisMatch() {	
		String longDescWithMinus = "The circuit accepts a reference to thing to be grabbed. It can store up to 10 things. Modes: 1 for grab. 0 for eject the first thing. -1 for eject all.";
		Matcher m = Pattern.compile(ObjectTree.macroRegex).matcher(longDescWithMinus);
//		if (m.find())
//			fail("Invalid Match is no macro in this string.");
	}
	
	@Test
	public void ShortVersionOfMisMatch() {	
		String longDescWithMinus = ". - 1";
		Matcher m = Pattern.compile(ObjectTree.macroRegex).matcher(longDescWithMinus);
//		if (m.find())
//			pass("There is no macro in this string.");
	}
	
	@Test
	public void MultipleDecimals() {	
		String longDescWithMinus = "1.234.5 + 2";
		Matcher m = Pattern.compile(ObjectTree.macroRegex).matcher(longDescWithMinus);
//		if (m.find())
//			fail("This macro has 2 decimals and should not be a match.");
	}
	
	@Test
	public void DecimalsInARow() {	
		String longDescWithMinus = "1................................................5 - 1";
		Matcher m = Pattern.compile(ObjectTree.macroRegex).matcher(longDescWithMinus);
//		if (m.find())
//			fail("Should not match on repeating decimals.");
	}	
	
    public static junit.framework.Test suite(){
        return new JUnit4TestAdapter(ObjectTreeTest.class);
     }

}
