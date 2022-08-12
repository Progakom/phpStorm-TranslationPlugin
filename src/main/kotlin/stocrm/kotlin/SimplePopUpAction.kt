package stocrm.kotlin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent

var PopUpDialogInstance: PopUpDialog? = null
var DocumentModifierInstance: DocumentModifier? = null


internal class SimplePopUpAction : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        DocumentModifierInstance = DocumentModifier(e);
        val selectedText = DocumentModifierInstance?.getSelectedText().toString()
        if (selectedText.isNotEmpty()) {
            PopUpDialogInstance = PopUpDialog(e.project, templatePresentation.text, selectedText)
            PopUpDialogInstance?.show()
        }
    }

}

class PopUpDialog(project: Project?, dialogTitle: String, selectedText: String) :
    DialogWrapper(project, null, true, IdeModalityType.MODELESS, false) {

    private var selectedText = ""

    init {
        title = dialogTitle
        this.selectedText = selectedText
        isModal = true
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = simplePopUp(this.selectedText)
        val jPanel = JBPanel<DialogPanel>()
        jPanel.minimumSize = Dimension(400, 300)
        jPanel.preferredSize = Dimension(400, 300)
        jPanel.add(panel, BorderLayout.PAGE_START)
        return jPanel
    }

}