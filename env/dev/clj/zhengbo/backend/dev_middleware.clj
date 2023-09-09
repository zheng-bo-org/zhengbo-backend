(ns zhengbo.backend.dev-middleware)

(defn wrap-dev [handler _opts]
  (-> handler))
