package stocrm.kotlin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicReference

class DocumentModifier(var event: AnActionEvent) {

    fun getSelectedText(): String {
        val selectedText = AtomicReference("")
        try {
            var editor: Editor = event.getRequiredData(CommonDataKeys.EDITOR)
            val project: Project = event.getRequiredData(CommonDataKeys.PROJECT)
            val document = editor.document

            // Work off of the primary caret to get the selection info
            val primaryCaret = editor.caretModel.primaryCaret
            val start = primaryCaret.selectionStart
            val end = primaryCaret.selectionEnd

            if (start <= 0 || end <= 0 || start == end) {
                throw error("Текст не выбран")
            } else {
                WriteCommandAction.runWriteCommandAction(
                    project
                ) {
                    selectedText.set(
                        URLEncoder.encode(
                            document.getText(TextRange.create(start, end)), StandardCharsets.UTF_8
                        )
                    )
                }
            }
        }catch (e: Throwable){
            Messages.showMessageDialog(e.message, "Error", Messages.getInformationIcon())
        }

        return selectedText.toString()
    }

    fun replaceSelectedText(replacement: String) {
        val editor: Editor = event.getRequiredData(CommonDataKeys.EDITOR)
        val document = editor.document
        val project = event.getRequiredData(CommonDataKeys.PROJECT)

        // Work off of the primary caret to get the selection info
        val primaryCaret = editor.caretModel.primaryCaret
        val start = primaryCaret.selectionStart
        val end = primaryCaret.selectionEnd

        WriteCommandAction.runWriteCommandAction(project) {
            document.replaceString(start, end, replacement);
        }

        // De-select the text range that was just replaced
        primaryCaret.removeSelection()
    }
}