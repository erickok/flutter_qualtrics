package be.vrt.flutter_qualtrics

import androidx.annotation.NonNull
import io.flutter.plugin.common.MethodCall

object Extractor {
    private enum class QualtricsPluginCall(val rawMethodName: String? = null) {
        INIT("init"),
        EVALUATE_TARGETING_LOGIC("evaluateTargetingLogic"),
        DISPLAY("display"),
        DISPLAY_TARGET("displayTarget"),
        HIDE("hide"),
        REGISTER_VIEW_VISIT("registerViewVisit"),
        RESET_TIMER("resetTimer"),
        RESET_VIEW_COUNTER("resetViewCounter"),
        SET_STRING_PROPERTY("setStringProperty"),
        SET_NUMBER_PROPERTY("setNumberProperty"),
        UNKNOWN(null);
    }

    sealed class QualtricsCall {
        data class Init(@NonNull val brandId: String, @NonNull val zoneId: String, @NonNull val interceptId: String) : QualtricsCall()
        object EvaluateTargetingLogic : QualtricsCall()
        object Display : QualtricsCall()
        data class DisplayTarget(@NonNull val target: String) : QualtricsCall()
        object Hide : QualtricsCall()
        data class RegisterViewVisit(@NonNull val viewName: String) : QualtricsCall()
        object ResetTimer : QualtricsCall()
        object ResetViewCounter : QualtricsCall()
        data class SetStringProperty(@NonNull val key: String, @NonNull val value: String) : QualtricsCall()
        data class SetNumberProperty(@NonNull val key: String, @NonNull val value: Double) : QualtricsCall()
        object Unknown : QualtricsCall()
    }

    private const val brandIdKey = "brandId"
    private const val zoneIdKey = "zoneId"
    private const val interceptIdKey = "interceptId"
    private const val targetKey = "target"
    private const val viewNameKey = "viewName"
    private const val keyKey = "key"
    private const val valueKey = "value"

    private fun callFromRawMethodName(rawMethodName: String?): QualtricsPluginCall =
            QualtricsPluginCall.values()
                    .filter { !it.rawMethodName.isNullOrEmpty() }
                    .firstOrNull { it.rawMethodName == rawMethodName }
                    ?: QualtricsPluginCall.UNKNOWN

    fun qualtricsCallFromCall(call: MethodCall): QualtricsCall =
            when (callFromRawMethodName(call.method)) {
                QualtricsPluginCall.INIT -> qualtricsCallFromInitCall(call)
                QualtricsPluginCall.EVALUATE_TARGETING_LOGIC -> QualtricsCall.EvaluateTargetingLogic
                QualtricsPluginCall.DISPLAY -> QualtricsCall.Display
                QualtricsPluginCall.DISPLAY_TARGET -> qualtricsCallFromDisplayTargetCall(call)
                QualtricsPluginCall.HIDE -> QualtricsCall.Hide
                QualtricsPluginCall.REGISTER_VIEW_VISIT -> qualtricsCallFromRegisterViewVisitCall(call)
                QualtricsPluginCall.RESET_TIMER -> QualtricsCall.ResetTimer
                QualtricsPluginCall.RESET_VIEW_COUNTER -> QualtricsCall.ResetViewCounter
                QualtricsPluginCall.SET_STRING_PROPERTY -> qualtricsCallFromSetStringProperty(call)
                QualtricsPluginCall.SET_NUMBER_PROPERTY -> qualtricsCallFromSetNumberProperty(call)
                QualtricsPluginCall.UNKNOWN -> QualtricsCall.Unknown
            }

    private fun qualtricsCallFromInitCall(call: MethodCall): QualtricsCall =
            QualtricsCall.Init(
                    call.argument(brandIdKey)!!,
                    call.argument(zoneIdKey)!!,
                    call.argument(interceptIdKey)!!
            )

    private fun qualtricsCallFromDisplayTargetCall(call: MethodCall): QualtricsCall =
            QualtricsCall.DisplayTarget(
                    call.argument(targetKey)!!
            )

    private fun qualtricsCallFromRegisterViewVisitCall(call: MethodCall): QualtricsCall =
            QualtricsCall.RegisterViewVisit(
                    call.argument(viewNameKey)!!
            )

    private fun qualtricsCallFromSetStringProperty(call: MethodCall): QualtricsCall =
            QualtricsCall.SetStringProperty(
                    call.argument(keyKey)!!,
                    call.argument(valueKey)!!
            )

    private fun qualtricsCallFromSetNumberProperty(call: MethodCall): QualtricsCall =
            QualtricsCall.SetNumberProperty(
                    call.argument(keyKey)!!,
                    call.argument(valueKey)!!
            )

}