package com.dolphinstudio.aicommerce.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import aicommerce.shared.generated.resources.Res
import aicommerce.shared.generated.resources.belt_black
import aicommerce.shared.generated.resources.blazer_black
import aicommerce.shared.generated.resources.cables_black
import aicommerce.shared.generated.resources.case_blue
import aicommerce.shared.generated.resources.coat_blue
import aicommerce.shared.generated.resources.cushion_white
import aicommerce.shared.generated.resources.dress_blue
import aicommerce.shared.generated.resources.earbuds_black
import aicommerce.shared.generated.resources.earbuds_white
import aicommerce.shared.generated.resources.headphones_black
import aicommerce.shared.generated.resources.headphones_blue
import aicommerce.shared.generated.resources.headphones_gaming
import aicommerce.shared.generated.resources.headphones_white
import aicommerce.shared.generated.resources.jacket_black
import aicommerce.shared.generated.resources.knit_black
import aicommerce.shared.generated.resources.laptop_black
import aicommerce.shared.generated.resources.laptop_blue
import aicommerce.shared.generated.resources.laptop_gaming
import aicommerce.shared.generated.resources.laptop_silver
import aicommerce.shared.generated.resources.mouse_black
import aicommerce.shared.generated.resources.shirt_white
import aicommerce.shared.generated.resources.shoes_black_run
import aicommerce.shared.generated.resources.shoes_blue_run
import aicommerce.shared.generated.resources.shoes_sandals
import aicommerce.shared.generated.resources.shoes_white_court
import aicommerce.shared.generated.resources.shoes_white_walk
import aicommerce.shared.generated.resources.sleeve_black
import aicommerce.shared.generated.resources.strap_blue
import aicommerce.shared.generated.resources.tee_white
import aicommerce.shared.generated.resources.tote_white
import org.jetbrains.compose.resources.painterResource

@Composable
fun productPainter(imageKey: String): Painter? {
    val resource = when (imageKey) {
        "headphones_black" -> Res.drawable.headphones_black
        "headphones_white" -> Res.drawable.headphones_white
        "headphones_blue" -> Res.drawable.headphones_blue
        "headphones_gaming" -> Res.drawable.headphones_gaming
        "earbuds_black" -> Res.drawable.earbuds_black
        "earbuds_white" -> Res.drawable.earbuds_white
        "shoes_black_run" -> Res.drawable.shoes_black_run
        "shoes_white_walk" -> Res.drawable.shoes_white_walk
        "shoes_blue_run" -> Res.drawable.shoes_blue_run
        "shoes_white_court" -> Res.drawable.shoes_white_court
        "shoes_sandals" -> Res.drawable.shoes_sandals
        "laptop_silver" -> Res.drawable.laptop_silver
        "laptop_black" -> Res.drawable.laptop_black
        "laptop_gaming" -> Res.drawable.laptop_gaming
        "laptop_blue" -> Res.drawable.laptop_blue
        "shirt_white" -> Res.drawable.shirt_white
        "dress_blue" -> Res.drawable.dress_blue
        "knit_black" -> Res.drawable.knit_black
        "blazer_black" -> Res.drawable.blazer_black
        "coat_blue" -> Res.drawable.coat_blue
        "tee_white" -> Res.drawable.tee_white
        "jacket_black" -> Res.drawable.jacket_black
        "sleeve_black" -> Res.drawable.sleeve_black
        "mouse_black" -> Res.drawable.mouse_black
        "belt_black" -> Res.drawable.belt_black
        "strap_blue" -> Res.drawable.strap_blue
        "cushion_white" -> Res.drawable.cushion_white
        "tote_white" -> Res.drawable.tote_white
        "cables_black" -> Res.drawable.cables_black
        "case_blue" -> Res.drawable.case_blue
        else -> null
    }
    return resource?.let { painterResource(it) }
}
