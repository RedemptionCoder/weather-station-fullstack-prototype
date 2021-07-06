/**
 * The HttpDataFetcher object provides the ability to retrieve data from web pages. It will 
 * search for elements on the page that contains an id, class, or any other attribute with a 
 * string assigned to it that matches a string in the elementIds array. If a matching string is
 * found, it will retrieve the text/string between the next > and < characters. 
 * 
 * @param {*} URL The URL to attempt to retrieve the data from
 * @param {*} elementIds An array of element ids or class descriptions/names to look for
 */
 function HttpDataFetcher(URL, elementIds) {
    
    // The url containing the data to retrieve
    this.url = URL;

    // The array containing the element ids to search for
    this.ids = elementIds;

    // The response from the url
    this.responePage = ""; 

    // The retrieved data object
    this.data = {lastError:"", isSuccessfull:false};

    /**
     * use the xmlhttprequest object to get a response 
     * from the url. Assign the response to this objects 
     * response attribute
     */
    this.loadData = function() {
        
        // use Get method as this enables for bookmarking etc.
        // there is no sensitive data here. 

        // Create the AJAX request object
        let lHttpRequest = new XMLHttpRequest();

        // assign this object to the httpRequest object
        lHttpRequest.httpDataFetcher = this;

        // Tell the request object what to do
        lHttpRequest.onreadystatechange = function() {
                
            if (this.readyState == 4 && this.status == 200) {
                         
                // Assign the reponse to the responsePage attribute
                this.httpDataFetcher.responePage = this.responseText;

                // Process the data returned by the server
                this.httpDataFetcher.getData();
            }
        }

        // Open the request object
        lHttpRequest.open("GET", this.url, true);

        // Set the access control allow origin header
        // lHttpRequest.setRequestHeader("Referer: ","http://www.bom.gov.au/products/IDV60901/IDV60901.94865.shtml");

        // Send the request
        lHttpRequest.send();

    }

    /**
     * search the contents of the page for elements
     * containing labels or ids matching the supplied element
     */
    this.getData = function() {

        // Search for the values of the ids.
        this.ids.forEach(this.getElementInnerHTML);
        
    }

    /**
     * The callback function used by the built-in forEach function of 
     * the ids array. This function searches for a HTML element in the 
     * response page attribute containing the id that matches the value. 
     * Then it retrieves the inner HTML and assigns it to the data object
     * @param {string} value 
     * @param {integer} index 
     * @param {object} array 
     */
    this.getElementInnerHTML = function(value, index, array) {

        // Get the length of the id 
        let lIdLength = value.length;

        // Find the first occurence of the id
        let lPos = this.responePage.indexOf(value);
        let lLastIndex = -1;

        // Add the id name as an attribute of the data object
        // and assign an empty string to it for now
        this.data.value = "";

        // If the id was found 
        if (lPos > -1) {

            // Check length of responsePage is greater than lPos+lIdLength
            // to avoid index out of range
            if (this.responePage.length > (lPos+lIdLength)) {
                // Look for the closing tag character >
                lPos = this.responePage.indexOf(">", lPos + lIdLength + 1);
            }

            // if closing tag character was found
            if (lPos > -1) {

                // Look for the < character of the closing tag
                lLastIndex = this.responePage.indexOf("<", lPos);
                
                // If the < character was found
                if (lLastIndex > -1) {
                    
                    // Get the text between the closing tag character > and the opening
                    // < character of the closing tag. 
                    let lInnerHTML = this.responePage.slice(lPos+1, lLastIndex);
                    
                    // Assign the value to the id attribute
                    this.data.value = lInnerHTML;

                    // set isSuccessfull to true, as at least one value was found
                    this.data.isSuccessfull = true;
            
                }
            }
        }

    }

    /**
     * Refresh the data from the URL
     */
    this.refresh = function() {
        this.loadData();
    }

    /**
     * Gets the text inside the element containing the supplied id
     * @param {String} elementId 
     */
    this.getValue = function(elementId) {
        
        // Check if the property exists
        if (this.data.hasOwnProperty(elementId)) {
            return this.data.elementId;
        }

        // Property not found or no value assigned to it 
        return "";
    }

}