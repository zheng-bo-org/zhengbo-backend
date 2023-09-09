(ns zhengbo.backend.web.routes.api
  (:require
    [zhengbo.backend.web.controllers.health :as health]
    [zhengbo.backend.web.middleware.exception :as exception]
    [zhengbo.backend.web.middleware.formats :as formats]
    [integrant.core :as ig]
    [reitit.coercion.malli :as malli]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.swagger :as swagger]
    [zhengbo.backend.web.controllers.users :as user-handlers]
    [zhengbo.backend.web.middleware.auth :as auth-middleware]))

(def route-data
  {:coercion   malli/coercion
   :muuntaja   formats/instance
   :swagger    {:id ::api}

   :middleware [
                ;; query-params & form-params
                parameters/parameters-middleware
                  ;; content-negotiation
                muuntaja/format-negotiate-middleware
                  ;; encoding response body
                muuntaja/format-response-middleware
                  ;; exception handling
                coercion/coerce-exceptions-middleware
                  ;; decoding request body
                muuntaja/format-request-middleware
                  ;; coercing response bodys
                coercion/coerce-response-middleware
                  ;; coercing request parameters
                coercion/coerce-request-middleware
                  ;; exception handling
                exception/wrap-exception
                auth-middleware/auth-middleware]})

;; Routes
(defn user-api-routes [_opts]
  (vector ["/users/roles", {:get user-handlers/get-roles :allow-access true}]))

(defn default-routers [_opts]
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "zhengbo.backend API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/health"
    {:get health/healthcheck!}]]
  )

(defn api-routes [_opts]
  ;;Defined the routers. The routers is a vector of vector data stucture
  ;;For example: [["router1", {:get handlerFunction}], ["router2" {:post handlerFunction}]]
  (concat (default-routers _opts)
          (user-api-routes _opts)))


(derive :reitit.routes/api :reitit/routes)

(defmethod ig/init-key :reitit.routes/api
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  [base-path route-data (api-routes opts)])
