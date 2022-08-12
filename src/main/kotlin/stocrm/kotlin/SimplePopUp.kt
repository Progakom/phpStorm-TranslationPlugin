// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package stocrm.kotlin

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bind
import com.intellij.ui.dsl.builder.panel
import javax.swing.JLabel

fun simplePopUp(selectedText: String): DialogPanel {
    lateinit var lbModel: JLabel
    lateinit var panel: DialogPanel
    lateinit var codeInput: JBTextField
    lateinit var selectedTextInput: JBTextField
    lateinit var localizedString: LocalizedString
    var language = "1"
    val settings = AppSettingsState.getInstance()

    fun Row.textField(text: String): Cell<JBTextField> {
        return textField()
                .apply { component.text = text }
    }

    panel = panel {

        row("Пользователь:") {
            label(settings.userId)
        }

        buttonsGroup(title = "Язык:") {
            row {
                radioButton("Русский", "1")
            }
            row {
                radioButton("English", "2")
            }
        }.bind({ language }, { language = it })

        row("Код ошибки:") {
            codeInput = textField().component
        }

        row("Выделенный текст:") {
            selectedTextInput = textField(selectedText).component
//            label(selectedText)
        }

        group("") {
            row {
                button("Проверить") {
                    panel.apply()
                    lbModel.text = ""
                    localizedString = LocalizedString(codeInput.text, selectedTextInput.text, language)
                    if (localizedString.isExist()) {
                        lbModel.text += "Код " + codeInput.text + " уже используется"
                    }else{
                        lbModel.text += "Код " + codeInput.text + " доступен для добавления"
                    }
                }
                button("Добавить") {
                    panel.apply()
                    lbModel.text = ""
                    localizedString = LocalizedString(codeInput.text, selectedTextInput.text, language)
                    if (localizedString.isExist()) {
                        lbModel.text += "Код " + codeInput.text + " уже используется"
                    }else{
                        val isAdded: Boolean = localizedString.addCode()
                        if(isAdded){
                            DocumentModifierInstance?.replaceSelectedText(codeInput.text)
                            PopUpDialogInstance?.close(200)
                        }
                    }
                }
//                button("Test") {
//                    panel.apply()
//                    DocumentModifierInstance?.replaceSelectedText(codeInput.text)
//                    PopUpDialogInstance?.close(200)
//                }
            }
            row {
                lbModel = label("").component
            }
        }
    }

    return panel
}

