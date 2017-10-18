package com.hrishikeshmishra.intellij.plugin.code.actions;

import com.hrishikeshmishra.intellij.plugin.code.model.actions.CodeDetail;
import com.hrishikeshmishra.intellij.plugin.code.service.GistUpdaterService;
import com.hrishikeshmishra.intellij.plugin.code.service.Notify;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;

public class GistUpdaterAction extends AnAction {

    private static final String GIST_URL = "\"https://gist.github.com/hrishikesh-mishra/";
    private final GistUpdaterService gistUpdaterService;


    public GistUpdaterAction() {
        gistUpdaterService = new  GistUpdaterService();
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        CodeDetail codeDetail = getCodeDetailFromContext(anActionEvent);
        if (codeDetail == null){
            Notify.warn("Gist ID", "No code detail found");
            return;
        }
        gistUpdaterService.update(codeDetail.getGistId(), codeDetail.getFileName(), codeDetail.getFileContent());
    }

    @Override
    public void update(AnActionEvent anActionEvent) {
        PsiFile psiFile = getPsiClassFromContext(anActionEvent);
        anActionEvent.getPresentation().setEnabled(psiFile != null);
    }

    private CodeDetail getCodeDetailFromContext(AnActionEvent anActionEvent) {

        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        PsiFile psiFile = getPsiClassFromContext(anActionEvent);

        int offset = editor.getCaretModel().getOffset();
        PsiElement psiElement = psiFile.findElementAt(offset);

        PsiElement scope = psiElement;
        String gistId = null;

        while (scope != null) {

            if (scope instanceof PsiDocComment) {
                gistId = getGistId((PsiDocComment) scope);
                break;
            }

            if (scope instanceof PsiMember ||
                    scope instanceof PsiMethodCallExpression ||
                    scope instanceof PsiFile) {

                Notify.warn("Gist ID", "Gist id not found.");
                return null;
            }
            scope = scope.getContext();
        }

        /** No Gist Id found **/
        if (gistId == null) {
            Notify.warn("Gist ID", "Gist id not found.");
            return null;
        }

        String fileContent = psiFile.getText();
        String fileName = psiFile.getVirtualFile().getName();

        CodeDetail codeDetail = new CodeDetail(fileName, fileContent, gistId);
        Notify.info("Gist ID", codeDetail.toString());
        return codeDetail;
    }

    private PsiFile getPsiClassFromContext(AnActionEvent anActionEvent) {

        PsiFile psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);

        if (psiFile == null || editor == null) {
            return null;
        }

        return psiFile;


    }


    public String getGistId(PsiDocComment comment) {
        if (comment == null) {
            return null;
        }
        PsiDocTag[] tags = comment.findTagsByName("see");
        for (PsiDocTag tag : tags) {
            PsiElement[] dataElements = tag.getDataElements();
            if (dataElements.length > 0 && dataElements[0] != null) {
                String value = dataElements[0].getText();
                if (value != null && value.startsWith(GIST_URL)) {
                    return value.replace(GIST_URL, "").replace("\"", "");
                }
            }
        }
        return null;
    }
}

