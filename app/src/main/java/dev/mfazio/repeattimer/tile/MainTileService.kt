package dev.mfazio.repeattimer.tile

import android.content.ComponentName
import androidx.core.content.ContextCompat
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.FontStyle
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.expression.ProtoLayoutExperimental
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.ButtonColors
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import dev.mfazio.repeattimer.R
import timber.log.Timber

private const val RESOURCES_VERSION = "0"

/**
 * Skeleton for a tile with no images.
 */
@OptIn(ExperimentalHorologistApi::class)
class MainTileService : SuspendingTileService() {

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ResourceBuilders.Resources {
        //TODO: Add in any resources needed here
        return ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .addIdToImageMapping(
                "timer-icon",
                ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(
                        ResourceBuilders.AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.icon)
                            .build()
                    ).build()
            )
            .build()
    }

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        val singleTileTimeline = TimelineBuilders.Timeline.Builder()
            .addTimelineEntry(
                TimelineBuilders.TimelineEntry.Builder()
                    .setLayout(
                        LayoutElementBuilders.Layout.Builder()
                            .setRoot(tileLayout(requestParams.deviceConfiguration))
                            .build()
                    )
                    .build()
            )
            .build()

        return TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(singleTileTimeline)
            .build()
    }

    @androidx.annotation.OptIn(ProtoLayoutExperimental::class)
    private fun tileLayout(deviceParameters: DeviceParametersBuilders.DeviceParameters): LayoutElement {
        val transparentColor = ContextCompat.getColor(
            applicationContext, android.R.color.transparent
        )
        return PrimaryLayout.Builder(deviceParameters)
            .setPrimaryLabelTextContent(
                LayoutElementBuilders.Text.Builder()
                    .setText("Repeat Timer")
                    .setFontStyle(
                        FontStyle.Builder()
                            .setSize(DimensionBuilders.SpProp.Builder().setValue(24F).build())
                            .setColor(
                                ColorBuilders.argb(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.timer_blue
                                    )
                                )
                            )
                            .build()
                    )
                    .build()
            )
            .setContent(
                Button.Builder(
                    this, Clickable.Builder().setOnClick(
                        ActionBuilders.launchAction(
                            ComponentName(
                                "dev.mfazio.repeattimer",
                                "dev.mfazio.repeattimer.presentation.MainActivity"
                            )
                        )
                    ).build()
                )
                    .setButtonColors(
                        ButtonColors.primaryButtonColors(
                            Colors(
                                transparentColor,
                                transparentColor,
                                transparentColor,
                                transparentColor
                            )
                        )
                    )
                    .setSize(DimensionBuilders.DpProp.Builder(48F).build())
                    .setImageContent("timer-icon")
                    .setContentDescription("Start timer button image")
                    .build()
            )
            .build()
    }
}