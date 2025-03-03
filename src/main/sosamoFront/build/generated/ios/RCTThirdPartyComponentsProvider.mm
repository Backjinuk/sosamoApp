/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */


#import <Foundation/Foundation.h>

#import "RCTThirdPartyComponentsProvider.h"
#import <React/RCTComponentViewProtocol.h>

@implementation RCTThirdPartyComponentsProvider

+ (NSDictionary<NSString *, Class<RCTComponentViewProtocol>> *)thirdPartyFabricComponents
{
  return @{
		@"RNCNaverMapArrowheadPath": NSClassFromString(@"RNCNaverMapArrowheadPath"), // @mj-studio/react-native-naver-map
		@"RNCNaverMapCircle": NSClassFromString(@"RNCNaverMapCircle"), // @mj-studio/react-native-naver-map
		@"RNCNaverMapGround": NSClassFromString(@"RNCNaverMapGround"), // @mj-studio/react-native-naver-map
		@"RNCNaverMapMarker": NSClassFromString(@"RNCNaverMapMarker"), // @mj-studio/react-native-naver-map
		@"RNCNaverMapPath": NSClassFromString(@"RNCNaverMapPath"), // @mj-studio/react-native-naver-map
		@"RNCNaverMapPolygon": NSClassFromString(@"RNCNaverMapPolygon"), // @mj-studio/react-native-naver-map
		@"RNCNaverMapPolyline": NSClassFromString(@"RNCNaverMapPolyline"), // @mj-studio/react-native-naver-map
		@"RNCNaverMapView": NSClassFromString(@"RNCNaverMapView"), // @mj-studio/react-native-naver-map
		@"RNGoogleSignInButton": NSClassFromString(@"RNGoogleSignInButtonComponentView"), // @react-native-google-signin/google-signin
		@"RNDatePicker": NSClassFromString(@"RNDatePicker"), // react-native-date-picker
		@"RNGestureHandlerButton": NSClassFromString(@"RNGestureHandlerButtonComponentView"), // react-native-gesture-handler
		@"RNCViewPager": NSClassFromString(@"RNCPagerViewComponentView"), // react-native-pager-view
		@"RNCSafeAreaProvider": NSClassFromString(@"RNCSafeAreaProviderComponentView"), // react-native-safe-area-context
		@"RNCSafeAreaView": NSClassFromString(@"RNCSafeAreaViewComponentView"), // react-native-safe-area-context
		@"RNSFullWindowOverlay": NSClassFromString(@"RNSFullWindowOverlay"), // react-native-screens
		@"RNSModalScreen": NSClassFromString(@"RNSModalScreen"), // react-native-screens
		@"RNSScreenContainer": NSClassFromString(@"RNSScreenContainerView"), // react-native-screens
		@"RNSScreenContentWrapper": NSClassFromString(@"RNSScreenContentWrapper"), // react-native-screens
		@"RNSScreenFooter": NSClassFromString(@"RNSScreenFooter"), // react-native-screens
		@"RNSScreen": NSClassFromString(@"RNSScreenView"), // react-native-screens
		@"RNSScreenNavigationContainer": NSClassFromString(@"RNSScreenNavigationContainerView"), // react-native-screens
		@"RNSScreenStackHeaderConfig": NSClassFromString(@"RNSScreenStackHeaderConfig"), // react-native-screens
		@"RNSScreenStackHeaderSubview": NSClassFromString(@"RNSScreenStackHeaderSubview"), // react-native-screens
		@"RNSScreenStack": NSClassFromString(@"RNSScreenStackView"), // react-native-screens
		@"RNSSearchBar": NSClassFromString(@"RNSSearchBar"), // react-native-screens
  };
}

@end
