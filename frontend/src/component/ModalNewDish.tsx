import React from 'react';
import {Modal} from 'antd';
import {useDispatch} from 'react-redux';
import {Category} from "../types/types";
import FormNewDish from "./FormNewDish";

interface ModalDishProperty {
    modal2Open: boolean;
    category: Category[];
    onClose: () => void;
}

const ModalNewDish: React.FC<ModalDishProperty> = ({modal2Open, category, onClose}) => {
    const dispatch = useDispatch();


    return (
        <Modal
            title="Добавить новое блюдо"
            centered
            open={modal2Open}
            footer={null}
            onCancel={onClose}
        >
            <FormNewDish dispatch={dispatch} category={category}/>
        </Modal>
    );
};

export default ModalNewDish;