(ns zhengbo.backend.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[backend starting]=-"))
   :start      (fn []
                 (log/info "\n-=[backend started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[backend has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
