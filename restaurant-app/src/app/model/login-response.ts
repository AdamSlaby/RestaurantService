export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  fullName: string;
  role: string;
  restaurantId: number;
}
