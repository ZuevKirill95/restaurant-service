import React, {useEffect, useState} from 'react';
import {Button, Input, message, Modal} from 'antd';
import orderService from '../services/orderService';
import {useDispatch} from 'react-redux';
import {Props} from "../types/types";

const ModalCancelOrder: React.FC<Props> = ({id}) => {
    const dispatch = useDispatch();
    const [loading, setLoading] = useState(false);
    const [open, setOpen] = useState(false);
    const [inputValue, setInputValue] = useState('');
    const [error, setError] = useState('');
    const [isUpdate, setIsUpdate] = useState(false);

    function showModal() {
        setOpen(true);
    }

    function handleOk() {
        if (inputValue.trim() === '') {
            setError('Please enter some text');
            return;
        }

        orderService.cancelOrderById(id, inputValue, dispatch).then(() => {
            console.log('Success: cancel');
            setIsUpdate(true);

        }, (error) => {
            const _content = (error.response && error.response.data)
            console.log(_content);
            message.error("id не найден");
        });

        setLoading(true);
        setTimeout(() => {
            setLoading(false);
            setOpen(false);
        }, 1500);
    }

    function handleCancel() {
        setOpen(false);
        setError('');
    }

    function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        setInputValue(e.target.value);
        setError('');
    }

    function handleKeyPress(event: React.KeyboardEvent<HTMLInputElement>) {
        if (event.key === 'Enter') {
            handleOk();
        }
    }

    // useEffect(() => {
    //     orderService.getListOrders(dispatch);
    //     setIsUpdate(false);
    // }, [isUpdate, dispatch]);

    return (
        <>
            <Button type="primary" onClick={showModal}>
                Отмена
            </Button>
            <Modal
                open={open}
                title="Отменить заказ"
                onOk={handleOk}
                onCancel={handleCancel}
                footer={[
                    <Button key="back" onClick={handleCancel}>
                        Отмена
                    </Button>,
                    <Button key="submit" type="primary" loading={loading} onClick={handleOk}>
                        Отправить
                    </Button>,
                ]}
            >
                <Input value={inputValue} onChange={handleChange} onKeyDown={handleKeyPress}
                       placeholder="Введите причину"/>
                {error && <p style={{color: 'red'}}>{error}</p>}
            </Modal>
        </>
    );
};

export default ModalCancelOrder;