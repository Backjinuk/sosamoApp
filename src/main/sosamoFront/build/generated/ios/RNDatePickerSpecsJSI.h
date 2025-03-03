/**
 * This code was generated by [react-native-codegen](https://www.npmjs.com/package/react-native-codegen).
 *
 * Do not edit this file as changes may cause incorrect behavior and will be lost
 * once the code is regenerated.
 *
 * @generated by codegen project: GenerateModuleH.js
 */

#pragma once

#include <ReactCommon/TurboModule.h>
#include <react/bridging/Bridging.h>

namespace facebook::react {


  class JSI_EXPORT NativeRNDatePickerCxxSpecJSI : public TurboModule {
protected:
  NativeRNDatePickerCxxSpecJSI(std::shared_ptr<CallInvoker> jsInvoker);

public:
  virtual jsi::Object getConstants(jsi::Runtime &rt) = 0;
  virtual void closePicker(jsi::Runtime &rt) = 0;
  virtual void openPicker(jsi::Runtime &rt, jsi::Object props) = 0;
  virtual void removeListeners(jsi::Runtime &rt, double type) = 0;
  virtual void addListener(jsi::Runtime &rt, jsi::String eventName) = 0;

};

template <typename T>
class JSI_EXPORT NativeRNDatePickerCxxSpec : public TurboModule {
public:
  jsi::Value create(jsi::Runtime &rt, const jsi::PropNameID &propName) override {
    return delegate_.create(rt, propName);
  }

  std::vector<jsi::PropNameID> getPropertyNames(jsi::Runtime& runtime) override {
    return delegate_.getPropertyNames(runtime);
  }

  static constexpr std::string_view kModuleName = "RNDatePicker";

protected:
  NativeRNDatePickerCxxSpec(std::shared_ptr<CallInvoker> jsInvoker)
    : TurboModule(std::string{NativeRNDatePickerCxxSpec::kModuleName}, jsInvoker),
      delegate_(reinterpret_cast<T*>(this), jsInvoker) {}


private:
  class Delegate : public NativeRNDatePickerCxxSpecJSI {
  public:
    Delegate(T *instance, std::shared_ptr<CallInvoker> jsInvoker) :
      NativeRNDatePickerCxxSpecJSI(std::move(jsInvoker)), instance_(instance) {

    }

    jsi::Object getConstants(jsi::Runtime &rt) override {
      static_assert(
          bridging::getParameterCount(&T::getConstants) == 1,
          "Expected getConstants(...) to have 1 parameters");

      return bridging::callFromJs<jsi::Object>(
          rt, &T::getConstants, jsInvoker_, instance_);
    }
    void closePicker(jsi::Runtime &rt) override {
      static_assert(
          bridging::getParameterCount(&T::closePicker) == 1,
          "Expected closePicker(...) to have 1 parameters");

      return bridging::callFromJs<void>(
          rt, &T::closePicker, jsInvoker_, instance_);
    }
    void openPicker(jsi::Runtime &rt, jsi::Object props) override {
      static_assert(
          bridging::getParameterCount(&T::openPicker) == 2,
          "Expected openPicker(...) to have 2 parameters");

      return bridging::callFromJs<void>(
          rt, &T::openPicker, jsInvoker_, instance_, std::move(props));
    }
    void removeListeners(jsi::Runtime &rt, double type) override {
      static_assert(
          bridging::getParameterCount(&T::removeListeners) == 2,
          "Expected removeListeners(...) to have 2 parameters");

      return bridging::callFromJs<void>(
          rt, &T::removeListeners, jsInvoker_, instance_, std::move(type));
    }
    void addListener(jsi::Runtime &rt, jsi::String eventName) override {
      static_assert(
          bridging::getParameterCount(&T::addListener) == 2,
          "Expected addListener(...) to have 2 parameters");

      return bridging::callFromJs<void>(
          rt, &T::addListener, jsInvoker_, instance_, std::move(eventName));
    }

  private:
    friend class NativeRNDatePickerCxxSpec;
    T *instance_;
  };

  Delegate delegate_;
};

} // namespace facebook::react
