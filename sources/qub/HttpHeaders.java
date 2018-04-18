package qub;

public interface HttpHeaders extends Iterable<HttpHeader>
{
    /**
     * Get the header in this collection that has the provided header name.
     * @param headerName The name of the header to get.
     * @return The header in this collection with the provided headerName, if the header exists in
     * this collection.
     */
    Result<HttpHeader> get(String headerName);

    /**
     * Get the value of the header in this collection that has the provided header name.
     * @param headerName The name of the header to get.
     * @return The value of header in this collection with the provided headerName, if the header
     * exists in this collection.
     */
    Result<String> getValue(String headerName);
}
