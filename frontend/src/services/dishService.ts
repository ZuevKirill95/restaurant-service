import axios from "axios";
import {Dispatch} from "redux";
import authHeader from "./auth-header";
import {setAllBranchDishes, setAllDish} from "../slices/dishSlice";
import {Dish} from "../types/types";

const API_URL_DISHES = "dishes"

async function getListDishByBranch(dispatch: Dispatch) {
    const headers = authHeader();

    try {
        const response = await axios.get(`${API_URL_DISHES}/all`, {headers});

        const dishes = response.data;
        dispatch(setAllBranchDishes(dishes));
        return dishes;

    } catch (error) {
        console.error("Ошибка получения всех блюд у филиала:", error);
        throw error;
    }
}

async function getListDish(page: number, size: number, dispatch: Dispatch) {
    const headers = authHeader();

    try {
        const response = await axios.get(`${API_URL_DISHES}/customer/any?page=${page}&size=${size}`, {headers});
        const dishes = response.data.content;
        dispatch(setAllDish(dishes));
        return dishes;

    } catch (error) {
        console.error("Ошибка получения всех блюд:", error);
        throw error;
    }
}

async function updateDish(dish: Dish, dispatch: Dispatch) {
    const headers = authHeader();

    try {
        return (await axios.put(API_URL_DISHES, dish, {headers})).data;

    } catch (error) {
        console.error("Ошибка обновления блюда:", error);
        throw error;
    }
}

async function addDish(values: Dish, dispatch: Dispatch) {
    const headers = authHeader();
    const dish = {
        "name": values.name,
        "urlImage": values.urlImage,
        "description": values.description,
        "category": {
            "id": values.category
        },
        "price": values.price,
        "weight": values.weight
    }
    try {
        return (await axios.post(`${API_URL_DISHES}/`, dish, {headers})).data;

    } catch (error) {
        console.error("Ошибка добавления блюда:", error);
        throw error;
    }
}

const dishService = {
    getListDishByBranch,
    getListDish,
    updateDish,
    addDish
};

export default dishService;