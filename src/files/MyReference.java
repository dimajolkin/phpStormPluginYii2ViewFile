package files;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MyReference implements PsiReference {
    private String uri;
    private PsiElement element;
    private TextRange textRange;
    private Project project;
    private VirtualFile appDir;

    MyReference(String uri, PsiElement element, TextRange textRange, Project project, VirtualFile appDir) {
        this.uri = uri;
        this.element = element;
        this.textRange = textRange;
        this.project = project;
        this.appDir = appDir;
    }

    public PsiElement getElement() {
        return this.element;
    }

    public TextRange getRangeInElement() {
        return this.textRange;
    }

    @Nullable
    public PsiElement resolve() {
        VirtualFile file = project.getWorkspaceFile();
        if (file == null) {
            return null;
        }

        String filePath = uri;
        file = project.getBaseDir().findFileByRelativePath(filePath);
        if (file == null) {
            return null;
        }

        return PsiManager.getInstance(project).findFile(file);
    }

    @NotNull
    public String getCanonicalText() {
        return this.uri;
    }

    public PsiElement handleElementRename(String s) throws IncorrectOperationException {
        return null;
    }

    public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        return null;
    }

    public boolean isReferenceTo(PsiElement psiElement) {
        return false;
    }

    @NotNull
    public Object[] getVariants() {
        return new Object[0];
    }

    public boolean isSoft() {
        return false;
    }
}
