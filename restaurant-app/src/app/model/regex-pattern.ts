export class RegexPattern {
  public static NAME: string = '^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$';
  public static SURNAME: string = '^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}(-[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32})?$';
  public static PHONE: string = '^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$';
  public static STREET: string = '^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż -.]{3,57}$';
  public static HOUSE_NR: string = '^[1-9]\\d*(?:[ -]?(?:[a-zA-Z]+|[0-9]\\d*))?$';
  public static FLAT_NR: string = '^[1-9]\\d*$';
  public static POSTCODE: string = '^\\d{2}-\\d{3}$';
}
