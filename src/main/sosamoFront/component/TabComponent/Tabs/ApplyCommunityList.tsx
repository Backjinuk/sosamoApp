import React, {useCallback, useMemo, useRef} from "react";
import {Button, StyleSheet, Text, View} from "react-native";
import BottomSheet, {BottomSheetModal, BottomSheetModalProvider, BottomSheetView} from "@gorhom/bottom-sheet";

export default function ApplyCommunityList() {
    // ref to control the BottomSheetModal
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);

    // defining snap points for the BottomSheetModal
    const snapPoints = useMemo(() => ['25%', '50%', '90%'], []);

    // function to open the BottomSheetModal
    const handlePresentModalPress = useCallback(() => {
        bottomSheetModalRef.current?.present();
    }, []);

    // callback when the BottomSheetModal changes
    const handleSheetChanges = useCallback((index: number) => {
        console.log('handleSheetChanges', index);
    }, []);

    // renders
    return (
        <BottomSheetModalProvider>
            <View style={styles.container}>
                <Button
                    onPress={handlePresentModalPress}
                    title="Open Community List"
                    color="black"
                />
                <BottomSheetModal
                    ref={bottomSheetModalRef}
                    index={1}
                    snapPoints={snapPoints}
                    onChange={handleSheetChanges}
                    backgroundStyle={styles.bottomSheetBackground}
                >
                    <View style={styles.contentContainer}>
                        <Text style={styles.title}>Community List 🎉</Text>
                        <View style={styles.item}>
                            <Text>Community 1</Text>
                        </View>
                        <View style={styles.item}>
                            <Text>Community 2</Text>
                        </View>
                        <View style={styles.item}>
                            <Text>Community 3</Text>
                        </View>
                    </View>
                </BottomSheetModal>
            </View>
        </BottomSheetModalProvider>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 24,
        justifyContent: 'center',
        backgroundColor: 'grey',
    },
    contentContainer: {
        flex: 1,
        padding: 16,
        backgroundColor: '#ffffff',
        alignItems: 'center',
    },
    bottomSheetBackground: {
        backgroundColor: '#f5f5f5',
    },
    title: {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 20,
    },
    item: {
        width: '100%',
        padding: 15,
        backgroundColor: '#e0e0e0',
        borderRadius: 10,
        marginBottom: 10,
    },
});
