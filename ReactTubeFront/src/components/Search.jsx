/* eslint-disable react/prop-types */

const Search = ({ setSearchTerm }) => {
  const handleInputChange = (event) => {
    setSearchTerm(event.target.value);
  };

  return (
    <div>
      <input type="text" onChange={handleInputChange} />
    </div>
  );
};

export default Search;
