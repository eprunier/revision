(ns revision.core
  (:require [revision.client.svn :as svn]))

(defmulti repository
  (fn [type _ _]
    (keyword type)))

(defmethod repository :svn [_ path options]
  (svn/get-repo path (:login options) (:password options)))

(defmethod repository :default [type _ _]
  (println "WARNING:" (name type) "is not implemented yet"))
