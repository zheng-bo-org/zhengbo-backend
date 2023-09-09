(ns zhengbo.backend.web.middleware.auth)

(defn token-is-valid? [token]
  false)

(defn permission? [path user-id]
  false)

(defn access-allowed? [request]
  (let [ [token-is-valid user-id] (token-is-valid? (get-in request [:headers :token]))]
    (if token-is-valid
      (permission? (:path request) user-id)
      false)))

(defn auth-middleware [handler]
  (fn [request]
    ()))