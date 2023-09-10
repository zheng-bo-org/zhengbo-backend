(ns zhengbo.backend.web.controllers.systems
  (:require [ring.util.http-response :as http-response]))


(defn get-roles
  [req]
  (http-response/ok {:roles [:TEACHER :PARENT :STUDENT]}))