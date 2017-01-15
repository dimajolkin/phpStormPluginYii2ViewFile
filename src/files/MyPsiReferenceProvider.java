package files;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;


public class MyPsiReferenceProvider extends PsiReferenceProvider {
    @NotNull
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull final ProcessingContext context) {

        Project project = element.getProject();
        PropertiesComponent properties = PropertiesComponent.getInstance(project);

        String yiiAppDir = properties.getValue("appPath", "app/");

        VirtualFile appDir = project.getBaseDir().findFileByRelativePath(yiiAppDir);

        if (appDir == null) {
            return PsiReference.EMPTY_ARRAY;
        }

        String className = element.getClass().getName();
        Class elementClass = element.getClass();

        // определяем, что объект является экземпляром StringLiteralExpressionImpl
        if (className.endsWith("StringLiteralExpressionImpl")) {
            try {
                // Вызываем метод getValueRange, чтобы получить символьный диапазон, в котором находится наша ссылка
                Method method = elementClass.getMethod("getValueRange");
                Object obj = method.invoke(element);
                TextRange textRange = (TextRange) obj;
                Class _PhpPsiElement = elementClass.getSuperclass().getSuperclass().getSuperclass();
                // Вызываем метод getText, чтобы получить значение PHP-строки
                Method phpPsiElementGetText = _PhpPsiElement.getMethod("getText");
                Object obj2 = phpPsiElementGetText.invoke(element);
                String str = obj2.toString();
                String uri = yiiAppDir + str.substring(textRange.getStartOffset(), textRange.getEndOffset()) + ".html";
                int start = textRange.getStartOffset();
                int len = textRange.getLength();

                PluginManager.getLogger().warn(uri);
                // Проверяем, подходит ли нам данная PHP-строка (путь к шаблону) или нет
                if (isViewFactoryCall(element)) {
                    PsiReference ref = new MyReference(uri, element, new TextRange(start, start + len), project, appDir);
                    return new PsiReference[]{ref};
                }

            } catch (Exception e) {
                PluginManager.getLogger().warn(e);
            }
        }


        return PsiReference.EMPTY_ARRAY;
    }

    public static boolean isViewFactoryCall(PsiElement element) {
        PsiElement prevEl = element.getParent();

        String elClassName;
        if (prevEl != null) {
            elClassName = prevEl.getClass().getName();
        }
        prevEl = prevEl.getParent();
        if (prevEl != null) {
            elClassName = prevEl.getClass().getName();
            if (elClassName.endsWith("MethodReferenceImpl")) {
                try {

                    Method phpPsiElementGetName = prevEl.getClass().getMethod("getName");
                    String name = (String) phpPsiElementGetName.invoke(prevEl);
                    if (name.toLowerCase().equals("factory")) {

                        Method getClassReference = prevEl.getClass().getMethod("getClassReference");
                        Object classRef = getClassReference.invoke(prevEl);
                        //PrintElementClassDescription(classRef);
                        String phpClassName = (String) phpPsiElementGetName.invoke(classRef);
                        if (phpClassName.toLowerCase().equals("view")) {
                            return true;
                        }

                    }
                } catch (Exception ex) {

                }
            }
        }
        return false;
    }
}
