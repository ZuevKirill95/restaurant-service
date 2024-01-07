import axios from "axios";
import {Dispatch} from "redux";
import authHeader from "./auth-header";
import {setListCategory} from "../slices/categorySlice";

const API_URL_CATEGORY = "categories"

async function getListDishByBranchId(dispatch: Dispatch) {
    const headers = authHeader();

    try {
        const response = await axios.get(API_URL_CATEGORY, {headers});
        const category = response.data;
        dispatch(setListCategory(category));
        return category;

    } catch (error) {
        console.error("Ошибка получения всех категорий:", error);
        throw error;
    }
}

const dishService = {
    getListDishByBranch: getListDishByBranchId
};

export default dishService;