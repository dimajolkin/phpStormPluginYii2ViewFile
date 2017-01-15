package files;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;


public class MyPsiReferenceContributor extends PsiReferenceContributor {

    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        PluginManager.getLogger().warn("start plugin");
        registrar.registerReferenceProvider(StandardPatterns.instanceOf(PsiElement.class), new MyPsiReferenceProvider());
    }
}
