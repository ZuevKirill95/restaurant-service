import axios from "axios";
import authHeader from "./auth-header";

const API_URL_ORDER = "analytic"

async function getCountOrderFromEmployeeRestaurant(): Promise<number> {
    const headers = authHeader();

    try {
        const response = await axios.get(`${API_URL_ORDER}/employee`, {headers});

        return response.data;
    } catch (error) {
        console.error("Ошибка получения количества заказов у работника:", error);
        throw error;
    }
}

async function getOrderPerMonth(year: number|null, month: number|null): Promise<number> {
    const headers = authHeader();

    try {
        if (year && month){
            const response = await axios.get(`${API_URL_ORDER}/orders/per/month?month=${month}&year=${year}`, {headers});
            return response.data;
        } else {
            const response = await axios.get(`${API_URL_ORDER}/orders/per/month?month=`, {headers});
            return response.data;
        }
    } catch (error) {
        console.error("Ошибка получения количества заказов за месяц: ", error);
        throw error;
    }
}

const analyticService = {
    getCountOrderFromEmployeeRestaurant,
    getOrderPerMonth,
};

export default analyticService;