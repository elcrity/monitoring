export function call (url, method) {
    let headers = new Headers({
        "Content-Type": "application/json",
      });
      let options = {
        headers: headers,
        url: url,
        method: method,
      };
    return fetch(options.url, options)
     .then((response) => {
        if (response.ok) {
          return response.json();
        }else{
            throw response;
        }
      });
};