export const presets = ['module:metro-react-native-babel-preset'];
export const plugins = [
  // Pluggin to transform private methods in classes
  ['@babel/plugin-transform-private-methods', {loose: true}],
  // Required plugin for React Native Reanimated
  'react-native-reanimated/plugin'
];
