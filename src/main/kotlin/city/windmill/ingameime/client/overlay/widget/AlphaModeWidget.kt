package city.windmill.ingameime.client.overlay.widget

import city.windmill.ingameime.client.jni.ExternalBaseIME
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
class AlphaModeWidget(textRenderer: TextRenderer) : Widget(textRenderer) {
    private val text get() = I18n.translate(if (ExternalBaseIME.AlphaMode) "alpha.ingameime.mode" else "native.ingameime.mode")
    private var job: Job? = null
    
    override var active = false
        set(value) {
            job?.cancel()
            if (value) {
                job = GlobalScope.launch {
                    delay(3.seconds)
                    field = false
                }
            }
            field = value
        }
    override val width
        get() = with(super.width + textRenderer.getWidth(text)){
            if (this < height) height else this
        }
    override val height
        get() = super.height + textRenderer.fontHeight
    override val padding: Pair<Int, Int>
        get() = 2 to 3
    
    @Suppress("NAME_SHADOWING")
    override fun draw(matrices: MatrixStack?, offsetX: Int, offsetY: Int, mouseX: Int, mouseY: Int, delta: Float) {
        super.draw(matrices, offsetX, offsetY, mouseX, mouseY, delta)
        val offsetX = offsetX + width / 2 - textRenderer.getWidth(text) / 2
        val offsetY = offsetY + padding.second
        textRenderer.draw(matrices, text, offsetX.toFloat(), offsetY.toFloat(), textColor)
    }
}