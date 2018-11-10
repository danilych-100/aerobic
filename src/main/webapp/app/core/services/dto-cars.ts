/**
 * The interface of the car coming from the server.
 */
export interface ICarDto {
  id: number;
  make: ICarMakeDto;
  car_model: ICarModelDto;
  body_type: ICarBodyTypelDto;
  mileage: number;
  description: string;
  year: number;
  created_at: string;
  updated_at: string;
}

/**
 * The interface of the car send (post,put) to the server.
 */
export interface ICarPostDto {
  id?: number;
  make_id: number;
  car_model_id: number;
  body_type_id: number;
  mileage: number;
  description: string;
  year: number;
  created_at?: string;
  updated_at?: string;
}

/**
 * The interface of the CarMake coming from the server.
 */
export interface ICarMakeDto {
  id: number;
  name: string;
}

/**
 * The interface of the CarModel coming from the server.
 */
export interface ICarModelDto {
  id: number;
  name: string;
  make_id: number;
}

/**
 * The interface of the carBodyType coming from the server.
 */
export interface ICarBodyTypelDto {
  id: number;
  name: string;
}
