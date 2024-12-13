package fox.mods.mixin.client;

import fox.mods.KineticHostingAdditionClient;
import fox.mods.configuration.ReplaceButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class KineticHostingButton extends Screen {

    protected KineticHostingButton(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void replaceRealmsButton(int y, int spacingY, CallbackInfo ci) {
        for (ButtonWidget button : this.children().stream()
                .filter(element -> element instanceof ButtonWidget)
                .map(element -> (ButtonWidget) element)
                .toList()) {

            if (button.getMessage().getString().equals(ReplaceButton.REALMS())) {
                int x = button.getX();
                int yPos = button.getY();
                int buttonWidth = button.getWidth();
                int buttonHeight = button.getHeight();

                button.visible = false;

                int newX = KineticHostingAdditionClient.getLogoX();
                int newY = KineticHostingAdditionClient.getLogoY();
                int imageWidth = 120;
                int imageHeight = (int) (120 / KineticHostingAdditionClient.getLogoScale());

                if (KineticHostingAdditionClient.shouldDisplayLogo()) {
                    this.addDrawableChild(new TexturedButtonWidget(
                            newX, newY, imageWidth, imageHeight,
                            0, 0, imageHeight,
                            new Identifier("kinetic-hosting-addition", "textures/gui/image-logo.png"),
                            imageWidth, imageHeight,
                            (btn) -> Util.getOperatingSystem().open(KineticHostingAdditionClient.getAffiliateLink())
                    ));
                }

                this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("kinetic-hosting-addition.kineticButton"),
                        (btn) -> Util.getOperatingSystem().open(KineticHostingAdditionClient.getAffiliateLink())
                ).dimensions(x, yPos, buttonWidth, buttonHeight).build());
                break;
            }
        }
    }
}

