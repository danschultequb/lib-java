package qub;

public class QubFolder extends Folder
{
    private QubFolder(Folder qubFolder)
    {
        super(qubFolder.getFileSystem(), qubFolder.getPath());
    }

    public static QubFolder get(Folder qubFolder)
    {
        PreCondition.assertNotNull(qubFolder, "qubFolder");

        return new QubFolder(qubFolder);
    }

    public Result<File> getShortcutFile(String shortcutName)
    {
        PreCondition.assertNotNullAndNotEmpty(shortcutName, "shortcutName");

        return this.getFile(shortcutName);
    }

    /**
     * Get the publisher folders that are present in this QubFolder.
     *
     * @return The publisher folders that are present in this QubFolder.
     */
    public Iterator<QubPublisherFolder> iteratePublisherFolders()
    {
        return this.iterateFolders()
            .catchError(FolderNotFoundException.class)
            .map(QubPublisherFolder::get);
    }

    /**
     * Get a QubPublisherFolder for the publisher with the provided name.
     *
     * @param publisherName The name of the publisher.
     * @return The QubPublisherFolder for the publisher with the provided name.
     */
    public Result<QubPublisherFolder> getPublisherFolder(String publisherName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");

        return this.getFolder(publisherName)
            .then(QubPublisherFolder::get);
    }

    public Iterator<QubProjectFolder> iterateProjectFolders(String publisherName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");

        return LazyIterator.create(() ->
        {
            final QubPublisherFolder publisherFolder = this.getPublisherFolder(publisherName).await();
            return publisherFolder.iterateProjectFolders();
        });
    }

    public Result<QubProjectFolder> getProjectFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return Result.create(() ->
        {
            final QubPublisherFolder publisherFolder = this.getPublisherFolder(publisherName).await();
            return publisherFolder.getProjectFolder(projectName).await();
        });
    }

    public Result<Folder> getProjectDataFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return Result.create(() ->
        {
            final QubProjectFolder projectFolder = this.getProjectFolder(publisherName, projectName).await();
            return projectFolder.getProjectDataFolder().await();
        });
    }

    public Result<Folder> getProjectVersionsFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return this.getPublisherFolder(publisherName)
            .then((QubPublisherFolder publisherFolder) -> publisherFolder.getProjectVersionsFolder(projectName).await());
    }

    public Iterator<QubProjectVersionFolder> iterateProjectVersionFolders(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return LazyIterator.create(() ->
        {
            final QubProjectFolder projectFolder = this.getProjectFolder(publisherName, projectName).await();
            return projectFolder.iterateProjectVersionFolders();
        });
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String publisherName, String projectName, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return Result.create(() ->
        {
            final QubProjectFolder projectFolder = this.getProjectFolder(publisherName, projectName).await();
            return projectFolder.getProjectVersionFolder(version).await();
        });
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(String publisherName, String projectName, VersionNumber version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        return this.getProjectVersionFolder(publisherName, projectName, version.toString());
    }

    public Result<QubProjectVersionFolder> getProjectVersionFolder(ProjectSignature projectSignature)
    {
        PreCondition.assertNotNull(projectSignature, "projectSignature");

        return this.getProjectVersionFolder(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion());
    }

    public Result<QubProjectVersionFolder> getLatestProjectVersionFolder(String publisherName, String projectName)
    {
        PreCondition.assertNotNullAndNotEmpty(publisherName, "publisherName");
        PreCondition.assertNotNullAndNotEmpty(projectName, "projectName");

        return Result.create(() ->
        {
            final QubPublisherFolder projectFolder = this.getPublisherFolder(publisherName).await();
            return projectFolder.getLatestProjectVersionFolder(projectName).await();
        });
    }

    /**
     * Get the published project folders of the provided project signatures and their transitive
     * dependencies.
     * @param projectSignatures The signatures of the projects to start looking in.
     * @param getDependenciesFunction The function that will be used to get the dependencies of a
     *                                given project.
     * @param validateDependencies Whether to validate each project referenced in the dependency
     *                             graph.
     * @return The published project folders of the provided dependencies.
     */
    public Result<Iterable<QubProjectVersionFolder>> getAllDependencyFolders(Iterable<ProjectSignature> projectSignatures, Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction, boolean validateDependencies)
    {
        PreCondition.assertNotNull(projectSignatures, "projectSignatures");
        PreCondition.assertNotNull(getDependenciesFunction, "getDependenciesFunction");

        return Result.create(() ->
        {
            final List<QubProjectVersionFolder> result = List.create();

            if (!Iterable.isNullOrEmpty(projectSignatures))
            {
                final MutableMap<ProjectSignature,List<Node1<ProjectSignature>>> dependencyPaths = ListMap.create();
                final MutableMap<Tuple2<String,String>,List<VersionNumber>> dependencyVersions = ListMap.create();
                final List<Throwable> dependencyErrors = List.create();
                Traversal.createDepthFirstSearch((TraversalActions<Node1<ProjectSignature>,Void> actions, Node1<ProjectSignature> currentNode) ->
                {
                    final ProjectSignature dependency = currentNode.getValue();
                    dependencyPaths.getOrSet(dependency, List::create).await()
                        .add(currentNode);
                    if (validateDependencies)
                    {
                        final List<VersionNumber> dependencyVersionList = dependencyVersions.getOrSet(Tuple.create(dependency.getPublisher(), dependency.getProject()), List::create).await();
                        if (!dependencyVersionList.contains(dependency.getVersion()))
                        {
                            dependencyVersionList.add(dependency.getVersion());
                        }
                    }

                    final QubProjectVersionFolder dependencyFolder = this.getProjectVersionFolder(dependency).catchError().await();
                    if (dependencyFolder != null)
                    {
                        result.add(dependencyFolder);

                        final Iterable<ProjectSignature> nextDependencies = getDependenciesFunction.run(dependencyFolder)
                            .catchError(dependencyErrors::add)
                            .await();
                        if (!Iterable.isNullOrEmpty(nextDependencies))
                        {
                            for (final ProjectSignature nextDependency : nextDependencies)
                            {
                                if (!dependencyPaths.containsKey(nextDependency))
                                {
                                    actions.visitNode(Node1.create(nextDependency).setNode1(currentNode));
                                }
                            }
                        }
                    }
                }).iterate(projectSignatures.map(Node1::create)).await();

                if (validateDependencies)
                {
                    final Iterable<MapEntry<Tuple2<String, String>, List<VersionNumber>>> multipleVersionDependencies = dependencyVersions
                        .where((MapEntry<Tuple2<String, String>, List<VersionNumber>> entry) -> entry.getValue().getCount() > 1);
                    if (multipleVersionDependencies.any())
                    {
                        final InMemoryCharacterToByteStream errorMessage = InMemoryCharacterToByteStream.create();
                        final IndentedCharacterWriteStream indentedErrorMessage = IndentedCharacterWriteStream.create(errorMessage)
                            .setSingleIndent(" ");
                        for (final MapEntry<Tuple2<String, String>, List<VersionNumber>> dependency : multipleVersionDependencies)
                        {
                            final String publisher = dependency.getKey().getValue1();
                            final String project = dependency.getKey().getValue2();
                            final Iterable<VersionNumber> versions = dependency.getValue();
                            indentedErrorMessage.writeLine("Found more than one required version for package " + publisher + "/" + project + ":").await();
                            int number = 0;
                            for (final VersionNumber version : versions)
                            {
                                ++number;
                                final String numberString = number + ". ";
                                final ProjectSignature dependencySignature = ProjectSignature.create(publisher, project, version);
                                indentedErrorMessage.setCurrentIndent("");
                                indentedErrorMessage.writeLine(numberString + dependencySignature).await();
                                indentedErrorMessage.setCurrentIndent(Strings.repeat(' ', numberString.length()));

                                final List<ProjectSignature> dependencyPath = List.create();
                                Node1<ProjectSignature> dependencyNode = dependencyPaths.get(dependencySignature).await().first();
                                // Skip the first node because that's the dependency itself (not the path to the dependency).
                                dependencyNode = dependencyNode.getNode1();
                                while (dependencyNode != null)
                                {
                                    dependencyPath.insert(0, dependencyNode.getValue());
                                    dependencyNode = dependencyNode.getNode1();
                                }

                                for (final ProjectSignature pathProjectSignature : dependencyPath)
                                {
                                    indentedErrorMessage.increaseIndent();
                                    indentedErrorMessage.writeLine("from " + pathProjectSignature).await();
                                }
                            }
                        }
                        dependencyErrors.add(new RuntimeException(errorMessage.getText().await().trim()));
                    }

                    if (dependencyErrors.any())
                    {
                        throw ErrorIterable.create(dependencyErrors);
                    }
                }
            }

            return result;
        });
    }
}
