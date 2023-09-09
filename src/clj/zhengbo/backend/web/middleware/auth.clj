(ns zhengbo.backend.web.middleware.auth
  (:require [ring.util.http-response :as http-response]))



(defn token-is-valid? [token]
  [true nil])

(defn what-about-permission? [path user-id]
  true)

(defn access-allowed? [request]
  (let [ [token-is-valid user-id] (token-is-valid? (get-in request [:headers :token]))]
    (if token-is-valid
      (let [permission-is-fine (what-about-permission? (:path request) user-id)]
        (if permission-is-fine [true nil] [false http-response/forbidden!]))
      [false http-response/unauthorized!])))

(defn auth-middleware [handler]
  (fn [request]
    (let [[access-allowed tell-her-access-is-not-allowed] (access-allowed? request)]
      (if access-allowed
        (handler request)
        (tell-her-access-is-not-allowed {})))))