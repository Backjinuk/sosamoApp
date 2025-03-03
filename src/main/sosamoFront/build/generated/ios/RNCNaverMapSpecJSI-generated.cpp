/**
 * This code was generated by [react-native-codegen](https://www.npmjs.com/package/react-native-codegen).
 *
 * Do not edit this file as changes may cause incorrect behavior and will be lost
 * once the code is regenerated.
 *
 * @generated by codegen project: GenerateModuleCpp.js
 */

#include "RNCNaverMapSpecJSI.h"

namespace facebook::react {

static jsi::Value __hostFunction_NativeRNCNaverMapUtilCxxSpecJSI_setGlobalZIndex(jsi::Runtime &rt, TurboModule &turboModule, const jsi::Value* args, size_t count) {
  static_cast<NativeRNCNaverMapUtilCxxSpecJSI *>(&turboModule)->setGlobalZIndex(
    rt,
    count <= 0 ? throw jsi::JSError(rt, "Expected argument in position 0 to be passed") : args[0].asString(rt),
    count <= 1 ? throw jsi::JSError(rt, "Expected argument in position 1 to be passed") : args[1].asNumber()
  );
  return jsi::Value::undefined();
}

NativeRNCNaverMapUtilCxxSpecJSI::NativeRNCNaverMapUtilCxxSpecJSI(std::shared_ptr<CallInvoker> jsInvoker)
  : TurboModule("RNCNaverMapUtil", jsInvoker) {
  methodMap_["setGlobalZIndex"] = MethodMetadata {2, __hostFunction_NativeRNCNaverMapUtilCxxSpecJSI_setGlobalZIndex};
}


} // namespace facebook::react
