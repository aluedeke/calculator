/**
 * Copyright (c) 2008, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.android.calculator2;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.content.IntentFilter;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.graphics.Rect;
import android.test.TouchUtils;

import com.android.calculator2.Calculator;
import com.android.calculator2.CalculatorDisplay;
import com.android.calculator2.R;
import com.robotium.solo.Solo;

/**
 * Instrumentation tests for poking some buttons
 *
 */

public class CalculatorHitSomeButtons extends ActivityInstrumentationTestCase2<Calculator> {
    public boolean setup = false;
    private static final String TAG = "CalculatorTests";
    Calculator mActivity = null;
    Instrumentation mInst = null;

    private Solo solo;
    
    public CalculatorHitSomeButtons() {
        super(Calculator.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        mActivity = getActivity();
        mInst = getInstrumentation();

        solo = new Solo(mInst, mActivity);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    @LargeTest
    public void testPressSomeKeys() {
        Log.i(TAG, "Pressing some keys!");
        
        // Make sure that we clear the output
        press(KeyEvent.KEYCODE_ENTER);
        press(KeyEvent.KEYCODE_CLEAR);
        
        // 3 + 4 * 5 => 23
        press(KeyEvent.KEYCODE_3);
        press(KeyEvent.KEYCODE_PLUS);
        press(KeyEvent.KEYCODE_4);
        press(KeyEvent.KEYCODE_9 | KeyEvent.META_SHIFT_ON);
        press(KeyEvent.KEYCODE_5);
        press(KeyEvent.KEYCODE_ENTER);
        
        assertEquals(displayVal(), "23");
    }
    
    @LargeTest
    public void testTapSomeButtons() throws InterruptedException {
        Log.v(TAG, "Tapping some buttons!");

        // Make sure that we clear the output
        tap("=");
        tap("CLR");

        // 567 / 3 => 189
        tap("5");
        tap("6");
        tap("7");
        tap(mActivity.getText(R.string.minus));
        tap("3");
        tap("=");

        assertEquals(displayVal(), "564");
        
        // make sure we can continue calculations also
        // 189 - 789 => -600
        tap("-");
        tap("7");
        tap("8");
        tap("0");
        tap("=");
        
        // Careful: the first digit in the expected value is \u2212, not "-" (a hyphen)
        assertEquals(displayVal(), mActivity.getString(R.string.minus) + "600");
    }
  
    // helper functions
    private void press(int keycode) {
        solo.sendKey(keycode);
    }
    
    private  void tap(CharSequence character) {
        Log.i(TAG, "tap view " + character);

        solo.clickOnButton(character.toString());
    }
  
    private String displayVal() {
        CalculatorDisplay display = (CalculatorDisplay) mActivity.findViewById(R.id.display);
        assertNotNull(display);
        
        EditText box = (EditText) display.getCurrentView();
        assertNotNull(box);
        
        return box.getText().toString().trim();
    }
}

