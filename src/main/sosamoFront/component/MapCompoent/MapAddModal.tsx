import React from 'react';
import {Button, Modal, Text} from 'react-native';

export default function MapAddModal(props: any) {

  const addMoim = () => {
    const dummies = [...props.dummies, props.position];
    props.setDummies(dummies);
    props.closeAddForm();
    // console.log(dummies);
  };

  return (
    <>
      <Modal
        visible={props.state === 'add'}
        onRequestClose={() => props.closeAddForm()}>
        {props.position &&
          Object.entries(props.position).map((elem: any) => (
            <Text key={elem[0]}>
              {elem[0]} : {elem[1]}
            </Text>
          ))}
        <Button title="등록" onPress={addMoim} />
        <Button
          title="뒤로"
          onPress={() => {
            props.closeAddForm();
          }}
        />
      </Modal>
    </>
  );
}
