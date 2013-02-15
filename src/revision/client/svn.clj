(ns revision.svn
  (:import [org.tmatesoft.svn.core SVNURL]
           [org.tmatesoft.svn.core.io SVNRepositoryFactory SVNRepository]
           [org.tmatesoft.svn.core.wc SVNClientManager SVNWCUtil]
           [org.tmatesoft.svn.core.internal.io.fs FSRepositoryFactory]
           [org.tmatesoft.svn.core.internal.io.dav DAVRepositoryFactory DAVRepository]
           [org.tmatesoft.svn.core.internal.io.svn SVNRepositoryFactoryImpl])
  (:require [revision.model.repository]))

(DAVRepositoryFactory/setup)
(SVNRepositoryFactoryImpl/setup)
(FSRepositoryFactory/setup)

(defn- get-history
  "Get repository history"
  [^SVNRepository repo]
  (let [startRevision 0
        endRevision 5]
    (-> repo
        (.log (into-array [""]) nil startRevision endRevision true true))))

(defn- get-head
  "Get head revision"
  [^SVNRepository repo]
  (.getLatestRevision repo))

;;; Repository protocol implementation
(extend-protocol revision.model.repository/Repository
  org.tmatesoft.svn.core.io.SVNRepository
  (log [this]
    (get-history this))
  (head [this]
    (get-head this))
  (revision [this rev]
    (println "get-revision")))

(defn ^SVNRepository get-repo
  "Load SVN repository"
  ([uri]
     (get-repo uri "" ""))
  ([uri login password]
     (let [url (SVNURL/parseURIEncoded uri)
           repo (SVNRepositoryFactory/create url)]
       (when (or login password)
         (let [authManager (SVNWCUtil/createDefaultAuthenticationManager login password)]    
           (.setAuthenticationManager repo authManager)))
       repo)))
