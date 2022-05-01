import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GeneralService {
  public static BASE_URL = 'http://localhost:9000';

  constructor() { }
}
