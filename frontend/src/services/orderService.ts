import axios from "axios";
import {Dispatch} from "redux";
import authHeader from "./auth-header";
import {setAllOrders, updateOrders} from "../slices/orderSlice";
import {User} from "../types/types";

const API_URL_ORDER = "orders"

async function updateOrderStatusById(id: number, status: string, dispatch: Dispatch) {
    const headers = authHeader();
    try {
        const response = await axios.put(API_URL_ORDER + `/${id}`,
            {status: status},
            {headers});

        return response.data;

    } catch (error) {
        console.error("Ошибка обновления заказа:", error);
        throw error;
    }
}

async function cancelOrderById(id: number, message: string, dispatch: Dispatch): Promise<User> {
    const headers = authHeader();

    try {
        const response = await axios.put(API_URL_ORDER + `/${id}/cancel`,
            {message: message},
            {headers});

        return response.data;

    } catch (error) {
        console.error("Ошибка отмены заказа:", error);
        throw error;
    }
}

async function cancelOrderByListId(id: string, message: string, dispatch: Dispatch): Promise<User> {
    const headers = authHeader();

    try {
        const response = await axios.put(API_URL_ORDER + `/cancel?listId=${id}`,
            {message: message},
            {headers});

        return response.data;

    } catch (error) {
        console.error("Ошибка отмены заказов:", error);
        throw error;
    }
}

async function getListOrders(dispatch: Dispatch) {
    const headers = authHeader();

    try {
        const response = await axios.get(API_URL_ORDER, {headers});

        dispatch(setAllOrders(response.data));
        return response.data;
    } catch (error) {
        console.error("Ошибка получения заказов:", error);
    }
}

async function getListOrdersByNotify(dispatch: Dispatch): Promise<number> {
    const headers = authHeader();

    try {
        const response = await axios.get(`${API_URL_ORDER}/notify`, {headers});
        dispatch(updateOrders(response.data));

        return response.data.length;
    } catch (error) {
        console.error("Ошибка получения заказов по уведомлению: ", error);
        throw error;
    }
}

const orderService = {
    updateOrderStatusById,
    cancelOrderById,
    cancelOrderByListId,
    getListOrders,
    getListOrdersByNotify,
};

export default orderService;