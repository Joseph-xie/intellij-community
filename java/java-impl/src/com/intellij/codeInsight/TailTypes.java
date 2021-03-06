/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.codeInsight;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.simple.BracesTailType;
import com.intellij.codeInsight.completion.simple.ParenthesesTailType;
import com.intellij.codeInsight.completion.simple.RParenthTailType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiSwitchBlock;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.siyeh.ig.psiutils.SwitchUtils;
import org.jetbrains.annotations.NotNull;

public class TailTypes {
  public static final TailType CALL_RPARENTH = new RParenthTailType(){
    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES && editor.getDocument().getCharsSequence().charAt(tailOffset - 1) != '(';
    }
  };
  public static final TailType RPARENTH = new RParenthTailType(){
    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_PARENTHESES;
    }
  };
  public static final TailType IF_RPARENTH = new RParenthTailType(){
    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_IF_PARENTHESES;
    }
  };
  public static final TailType WHILE_RPARENTH = new RParenthTailType(){
    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_WHILE_PARENTHESES;
    }
  };
  public static final TailType CALL_RPARENTH_SEMICOLON = new RParenthTailType(){
    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES;
    }

    @Override
    public int processTail(final Editor editor, int tailOffset) {
      return insertChar(editor, super.processTail(editor, tailOffset), ';');
    }
  };

  public static final TailType SYNCHRONIZED_LPARENTH = new ParenthesesTailType() {
    @Override
    protected boolean isSpaceBeforeParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_BEFORE_SYNCHRONIZED_PARENTHESES;
    }

    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_SYNCHRONIZED_PARENTHESES;
    }
  };
  public static final TailType CATCH_LPARENTH = new ParenthesesTailType() {
    @Override
    protected boolean isSpaceBeforeParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_BEFORE_CATCH_PARENTHESES;
    }

    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_CATCH_PARENTHESES;
    }
  };
  public static final TailType SWITCH_LPARENTH = new ParenthesesTailType() {
    @Override
    protected boolean isSpaceBeforeParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_BEFORE_SWITCH_PARENTHESES;
    }

    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_SWITCH_PARENTHESES;
    }
  };
  public static final TailType WHILE_LPARENTH = new ParenthesesTailType() {
    @Override
    protected boolean isSpaceBeforeParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_BEFORE_WHILE_PARENTHESES;
    }

    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_WHILE_PARENTHESES;
    }
  };
  public static final TailType FOR_LPARENTH = new ParenthesesTailType() {
    @Override
    protected boolean isSpaceBeforeParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_BEFORE_FOR_PARENTHESES;
    }

    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_FOR_PARENTHESES;
    }
  };
  public static final TailType IF_LPARENTH = new ParenthesesTailType() {
    @Override
    protected boolean isSpaceBeforeParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_BEFORE_IF_PARENTHESES;
    }

    @Override
    protected boolean isSpaceWithinParentheses(final CommonCodeStyleSettings styleSettings, final Editor editor, final int tailOffset) {
      return styleSettings.SPACE_WITHIN_IF_PARENTHESES;
    }
  };
  private static final String ARROW = " -> ";
  public static final TailType CASE_ARROW = new TailType() {
    @Override
    public int processTail(Editor editor, int tailOffset) {
      Document document = editor.getDocument();
      document.insertString(tailOffset, ARROW);
      return moveCaret(editor, tailOffset, ARROW.length());
    }

    @Override
    public boolean isApplicable(@NotNull InsertionContext context) {
      Document document = context.getDocument();
      int length = document.getTextLength();
      CharSequence chars = document.getCharsSequence();
      int offset;
      for(offset = context.getTailOffset(); offset < length; offset++) {
        char c = chars.charAt(offset);
        if (c != '\n' && c != ' ' && c != '\t') {
          break;
        }
      }
      boolean hasArrow = offset + 2 < length && chars.subSequence(offset, offset + 2).toString().equals("->");
      return !hasArrow;
    }

    @Override
    public String toString() {
      return "CASE_ARROW";
    }
  };
  private static final TailType BRACES = new BracesTailType();
  public static final TailType FINALLY_LBRACE = BRACES;
  public static final TailType TRY_LBRACE = BRACES;
  public static final TailType DO_LBRACE = BRACES;

  public static TailType forSwitchLabel(@NotNull PsiSwitchBlock block) {
    return SwitchUtils.isRuleFormatSwitch(block) ? CASE_ARROW : TailType.CASE_COLON;
  }


  private TailTypes() {}
}
