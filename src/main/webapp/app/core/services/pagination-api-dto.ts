/**
 * The interface of the Pagination coming from the server.
 */
export interface IPaginationApiDto {
  total: number;
  count: number;
  per_page: number;
  current_page: number;
  total_pages: number;
  links: [string, string];
}
