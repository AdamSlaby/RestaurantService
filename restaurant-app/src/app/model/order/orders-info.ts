import { OnlineOrderInfo } from "./online-order-info";
import { RestaurantOrderInfo } from "./restaurant-order-info";

export interface OrdersInfo {
    restaurantOrder: RestaurantOrderInfo;
    onlineOrder: OnlineOrderInfo;
}