import {StyleSheet} from 'react-native';

interface StylesProps {
  BorderBottomColor: string;
}

const createStyles = () => {
  return StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: '#fff',
      alignItems: 'center',
      justifyContent: 'center',
      paddingHorizontal: 20,
    },
    title: {
      fontSize: 24,
      fontWeight: 'bold',
      marginBottom: 10,
    },
    subtitle: {
      fontSize: 20,
      marginBottom: 5,
    },
    description: {
      fontSize: 16,
      color: '#888',
      marginBottom: 20,
    },
    socialButtonsContainer: {
      flexDirection: 'row',
      marginBottom: 20,
    },
    socialButton: {
      marginHorizontal: 5,
    },
    icon: {
      width: 50,
      height: 50,
    },
    orText: {
      fontSize: 16,
      color: '#888',
      marginVertical: 10,
    },
    signupButton: {
      backgroundColor: '#eee',
      paddingVertical: 10,
      paddingHorizontal: 20,
      borderRadius: 5,
      marginVertical: 5,
      width: '100%',
      alignItems: 'center',
    },
    signupButtonText: {
      fontSize: 16,
    },
    loginText: {
      fontSize: 14,
      color: '#888',
      marginTop: 20,
    },
    input: {
      width: '100%',
      height: 40,
      borderColor: '#ddd',
      borderWidth: 1,
      borderRadius: 4,
      paddingHorizontal: 8,
      marginBottom: 12,
    },
    checkboxContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      marginBottom: 12,
    },
    checkboxLabel: {
      marginLeft: 8,
      fontSize: 14,
    },
    loginButton: {
      backgroundColor: '#000',
      paddingVertical: 12,
      paddingHorizontal: 32,
      borderRadius: 4,
      marginBottom: 16,
    },
    loginButtonText: {
      color: '#fff',
      fontSize: 16,
    },
    linkContainer: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      width: '100%',
      marginBottom: 16,
    },
    linkText: {
      color: '#0066cc',
    },
    simpleLoginText: {
      fontSize: 16,
      marginBottom: 12,
    },
    socialIconsContainer: {
      flexDirection: 'row',
      justifyContent: 'space-around',
      width: '100%',
      marginBottom: 16,
    },
    noticeText: {
      fontSize: 14,
      color: '#ff6a00',
    },
  });
};

export default createStyles;
