export class HtmlUtility {
  public static scrollIntoView(id: string): void {
    let element = document.getElementById(id);
    if (element)
      element.scrollIntoView({behavior: "smooth"});
  }
}
